package dev.acorn.desktop.render.pipelines

import dev.acorn.core.math.Color
import dev.acorn.core.math.Vec2
import dev.acorn.desktop.gl.mesh.LineBatchMesh
import dev.acorn.desktop.gl.shader.ShaderProgram
import dev.acorn.desktop.gl.shader.ShaderSources
import org.joml.Matrix4f
import org.lwjgl.opengl.GL20.glGetUniformLocation
import org.lwjgl.opengl.GL20.glUniformMatrix4fv
import org.lwjgl.system.MemoryStack

/**
 * Batched line rendering pipeline.
 *
 * Accumulates line vertex data on the CPU and flushes all lines
 * in a single draw call. Call [flush] before switching to quad
 * rendering to preserve render order.
 */
class LineBatchPipeline {
    companion object {
        private const val MAX_LINES = 2000
    }

    private val shader = ShaderProgram(ShaderSources.LINE_BATCH_VERT, ShaderSources.LINE_BATCH_FRAG)
    private val mesh = LineBatchMesh(MAX_LINES)
    private val buffer = FloatArray(MAX_LINES * LineBatchMesh.FLOATS_PER_LINE)

    private val projection = Matrix4f()
    private var lineCount = 0

    /**
     * Begins a new batch frame. Must be called once at the start of each frame.
     */
    fun begin(projection: Matrix4f) {
        this.projection.set(projection)
        lineCount = 0
    }

    /**
     * Queues a line for batched drawing. If the batch is full, flushes first.
     */
    fun draw(a: Vec2, b: Vec2, color: Color) {
        if (lineCount >= MAX_LINES) {
            flush()
        }

        val r = color.r
        val g = color.g
        val b_ = color.b
        val alpha = color.a

        val offset = lineCount * LineBatchMesh.FLOATS_PER_LINE

        // Vertex 1 (start point)
        buffer[offset + 0] = a.x
        buffer[offset + 1] = a.y
        buffer[offset + 2] = r
        buffer[offset + 3] = g
        buffer[offset + 4] = b_
        buffer[offset + 5] = alpha

        // Vertex 2 (end point)
        buffer[offset + 6] = b.x
        buffer[offset + 7] = b.y
        buffer[offset + 8] = r
        buffer[offset + 9] = g
        buffer[offset + 10] = b_
        buffer[offset + 11] = alpha

        lineCount++
    }

    /**
     * Flushes any accumulated lines to the GPU. Call this before drawing
     * quads to preserve render order.
     */
    fun flush() {
        if (lineCount == 0) return

        shader.use()

        MemoryStack.stackPush().use { stack ->
            val pBuf = stack.mallocFloat(16)
            projection.get(pBuf)
            glUniformMatrix4fv(glGetUniformLocation(shader.id, "uProjection"), false, pBuf)
        }

        mesh.upload(buffer, lineCount)

        lineCount = 0
    }

    /**
     * Ends the batch frame, flushing any remaining lines.
     */
    fun end() {
        flush()
    }
}
