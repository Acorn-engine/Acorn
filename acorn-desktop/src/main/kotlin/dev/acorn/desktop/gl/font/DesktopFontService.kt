package dev.acorn.desktop.gl.font

import dev.acorn.core.assets.FontHandle
import dev.acorn.core.assets.FontService
import dev.acorn.desktop.gl.texture.DesktopTexture
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.stb.STBTTPackContext
import org.lwjgl.stb.STBTTPackedchar
import org.lwjgl.stb.STBTruetype.*
import org.lwjgl.system.MemoryUtil

/**
 * Desktop implementation of [FontService] using STB TrueType.
 *
 * Bakes a glyph atlas for ASCII 32–126 at the requested pixel size and uploads
 * it to the GPU as an RGBA texture where R=G=B=255 and A=coverage. This makes
 * glyph color controlled entirely by the per-vertex tint in the sprite batch
 * shader (`FragColor = texture(uTex, vUV) * vColor`).
 */
class DesktopFontService : FontService {

    companion object {
        private const val ATLAS_SIZE = 512
        private const val FIRST_CHAR = 32
        private const val NUM_CHARS = 95  // ASCII 32–126 inclusive
    }

    override fun load(path: String, size: Float): FontHandle {
        val resource = javaClass.classLoader.getResource(path)
            ?: throw IllegalArgumentException("Font resource '$path' not found on classpath")

        val fontBytes = resource.openStream().use { it.readBytes() }
        val fontBuffer = BufferUtils.createByteBuffer(fontBytes.size)
        fontBuffer.put(fontBytes).flip()

        // Allocate a native buffer for glyph metrics (95 chars)
        val packedChars = STBTTPackedchar.malloc(NUM_CHARS)

        // Allocate grayscale atlas bitmap
        val atlasBitmap = MemoryUtil.memAlloc(ATLAS_SIZE * ATLAS_SIZE)
        try {
            val packContext = STBTTPackContext.malloc()
            try {
                check(stbtt_PackBegin(packContext, atlasBitmap, ATLAS_SIZE, ATLAS_SIZE, 0, 1)) {
                    "stbtt_PackBegin failed"
                }
                stbtt_PackFontRange(packContext, fontBuffer, 0, size, FIRST_CHAR, packedChars)
                stbtt_PackEnd(packContext)
            } finally {
                packContext.free()
            }

            // Convert 1-channel grayscale atlas to RGBA (R=G=B=255, A=gray)
            val rgbaBuffer = MemoryUtil.memAlloc(ATLAS_SIZE * ATLAS_SIZE * 4)
            try {
                for (i in 0 until ATLAS_SIZE * ATLAS_SIZE) {
                    val gray = atlasBitmap.get(i)
                    rgbaBuffer.put(0xFF.toByte())  // R
                    rgbaBuffer.put(0xFF.toByte())  // G
                    rgbaBuffer.put(0xFF.toByte())  // B
                    rgbaBuffer.put(gray)           // A = coverage
                }
                rgbaBuffer.flip()

                val texId = glGenTextures()
                glBindTexture(GL_TEXTURE_2D, texId)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
                glTexImage2D(
                    GL_TEXTURE_2D, 0, GL_RGBA,
                    ATLAS_SIZE, ATLAS_SIZE,
                    0, GL_RGBA, GL_UNSIGNED_BYTE, rgbaBuffer
                )
                glBindTexture(GL_TEXTURE_2D, 0)

                val atlasTexture = DesktopTexture(texId, ATLAS_SIZE, ATLAS_SIZE)
                return DesktopFont(atlasTexture, packedChars, ATLAS_SIZE, ATLAS_SIZE)
            } finally {
                MemoryUtil.memFree(rgbaBuffer)
            }
        } finally {
            MemoryUtil.memFree(atlasBitmap)
        }
    }
}
