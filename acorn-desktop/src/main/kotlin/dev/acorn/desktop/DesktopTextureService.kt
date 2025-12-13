package dev.acorn.desktop

import dev.acorn.core.assets.TextureHandle
import dev.acorn.core.assets.TextureService
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.stb.STBImage.*
import org.lwjgl.system.MemoryStack.stackPush
import java.nio.ByteBuffer

class DesktopTextureService: TextureService {
    override fun load(path: String): TextureHandle {
        val resource = javaClass.classLoader.getResource(path)
            ?: throw IllegalArgumentException("Texture resource $path not found")

        val bytes = resource.openStream().use { it.readBytes() }
        val buffer: ByteBuffer = BufferUtils.createByteBuffer(bytes.size)
        buffer.put(bytes)
        buffer.flip()

        stackPush().use { stack ->
            val w = stack.mallocInt(1)
            val h = stack.mallocInt(1)
            val channels = stack.mallocInt(1)

            stbi_set_flip_vertically_on_load(true)

            val image = stbi_load_from_memory(buffer, w, h, channels, 4)
                ?: error("Failed to load image: ${stbi_failure_reason()}")

            val width = w.get()
            val height = h.get()

            val textureID = glGenTextures()
            glBindTexture(GL_TEXTURE_2D, textureID)

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image)
            stbi_image_free(image)

            return DesktopTexture(textureID, width, height)
        }
    }
}