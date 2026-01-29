package dev.acorn.desktop.render.pipelines

import dev.acorn.core.math.Color
import dev.acorn.core.scene.Transform
import dev.acorn.desktop.gl.mesh.SpriteBatchMesh
import dev.acorn.desktop.gl.shader.ShaderProgram
import dev.acorn.desktop.gl.shader.ShaderSources
import dev.acorn.desktop.gl.texture.DesktopTexture
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.glBindTexture
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture
import org.lwjgl.opengl.GL20.glGetUniformLocation
import org.lwjgl.opengl.GL20.glUniformMatrix4fv
import org.lwjgl.system.MemoryStack
import kotlin.math.cos
import kotlin.math.sin

/**
 * Batched sprite rendering pipeline.
 *
 * Accumulates pre-transformed vertex data on the CPU and flushes
 * same-texture sprites in a single draw call. Texture changes or
 * a full buffer trigger an automatic flush.
 */
class SpriteBatchPipeline {
    companion object {
        private const val MAX_SPRITES = 1000
    }

    private val shader = ShaderProgram(ShaderSources.BATCH_VERT, ShaderSources.BATCH_FRAG)
    private val mesh = SpriteBatchMesh(MAX_SPRITES)
    private val buffer = FloatArray(MAX_SPRITES * SpriteBatchMesh.FLOATS_PER_SPRITE)

    private val projection = Matrix4f()
    private var spriteCount = 0
    private var currentTextureId = -1

    /**
     * Begins a new batch frame. Must be called once at the start of each frame.
     */
    fun begin(projection: Matrix4f) {
        this.projection.set(projection)
        spriteCount = 0
        currentTextureId = -1
    }

    /**
     * Queues a sprite for batched drawing. If the texture changes or the batch
     * is full, the current batch is flushed first.
     */
    fun draw(transform: Transform, tex: DesktopTexture, tint: Color, circleMask: Boolean) {
        if (tex.id != currentTextureId || spriteCount >= MAX_SPRITES) {
            flush()
            currentTextureId = tex.id
        }

        val px = transform.position.x
        val py = transform.position.y
        val sx = transform.scale.x
        val sy = transform.scale.y
        val rad = Math.toRadians(transform.rotationDeg.toDouble()).toFloat()
        val cosR = cos(rad)
        val sinR = sin(rad)

        // Unit quad corners in local space (-0.5 .. 0.5)
        // Scale, rotate, translate to world space
        val maskVal = if (circleMask) 1f else 0f
        val r = tint.r
        val g = tint.g
        val b = tint.b
        val a = tint.a

        val offset = spriteCount * SpriteBatchMesh.FLOATS_PER_SPRITE

        // Local positions and UVs for the 4 corners
        val localX = floatArrayOf(-0.5f, 0.5f, 0.5f, -0.5f)
        val localY = floatArrayOf(-0.5f, -0.5f, 0.5f, 0.5f)
        val uvX = floatArrayOf(0f, 1f, 1f, 0f)
        val uvY = floatArrayOf(0f, 0f, 1f, 1f)

        for (v in 0 until 4) {
            val lx = localX[v]
            val ly = localY[v]

            // Scale
            val scaledX = lx * sx
            val scaledY = ly * sy

            // Rotate
            val rotX = scaledX * cosR - scaledY * sinR
            val rotY = scaledX * sinR + scaledY * cosR

            // Translate
            val worldX = rotX + px
            val worldY = rotY + py

            val vi = offset + v * SpriteBatchMesh.FLOATS_PER_VERTEX
            buffer[vi + 0] = worldX      // pos.x
            buffer[vi + 1] = worldY      // pos.y
            buffer[vi + 2] = uvX[v]      // uv.x
            buffer[vi + 3] = uvY[v]      // uv.y
            buffer[vi + 4] = r           // color.r
            buffer[vi + 5] = g           // color.g
            buffer[vi + 6] = b           // color.b
            buffer[vi + 7] = a           // color.a
            buffer[vi + 8] = lx          // local.x
            buffer[vi + 9] = ly          // local.y
            buffer[vi + 10] = maskVal    // maskType
        }

        spriteCount++
    }

    /**
     * Flushes any accumulated sprites to the GPU. Call this before drawing
     * non-batched geometry (shapes, lines) to preserve render order.
     */
    fun flush() {
        if (spriteCount == 0) return

        shader.use()

        MemoryStack.stackPush().use { stack ->
            val pBuf = stack.mallocFloat(16)
            projection.get(pBuf)
            glUniformMatrix4fv(glGetUniformLocation(shader.id, "uProjection"), false, pBuf)
        }

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, currentTextureId)
        shader.uniform1i("uTex", 0)

        mesh.upload(buffer, spriteCount)

        glBindTexture(GL_TEXTURE_2D, 0)

        spriteCount = 0
    }

    /**
     * Ends the batch frame, flushing any remaining sprites.
     */
    fun end() {
        flush()
    }
}
