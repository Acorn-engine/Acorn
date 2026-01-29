package dev.acorn.desktop.gl.mesh

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays

/**
 * Dynamic mesh for batched line rendering.
 *
 * Vertex layout per vertex (6 floats):
 * - pos   (vec2)  — world position
 * - color (vec4)  — line color (RGBA)
 *
 * Uses GL_LINES mode (2 vertices per line, no index buffer needed).
 * Dynamic VBO is re-uploaded each flush.
 */
class LineBatchMesh(private val maxLines: Int) {
    private val vao: Int
    private val vbo: Int

    companion object {
        const val FLOATS_PER_VERTEX = 6
        const val VERTICES_PER_LINE = 2
        const val FLOATS_PER_LINE = FLOATS_PER_VERTEX * VERTICES_PER_LINE
    }

    init {
        vao = glGenVertexArrays()
        glBindVertexArray(vao)

        // Dynamic VBO — allocated to max size, data uploaded per flush
        vbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(
            GL_ARRAY_BUFFER,
            (maxLines * FLOATS_PER_LINE * 4).toLong(),
            GL_DYNAMIC_DRAW
        )

        // Vertex attributes
        val stride = FLOATS_PER_VERTEX * 4 // bytes
        var offset = 0L

        // location 0: aPos (vec2)
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, offset)
        glEnableVertexAttribArray(0)
        offset += 2 * 4

        // location 1: aColor (vec4)
        glVertexAttribPointer(1, 4, GL_FLOAT, false, stride, offset)
        glEnableVertexAttribArray(1)

        glBindVertexArray(0)
    }

    /**
     * Uploads vertex data and draws the specified number of lines.
     */
    fun upload(data: FloatArray, lineCount: Int) {
        glBindVertexArray(vao)
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferSubData(GL_ARRAY_BUFFER, 0, data)
        glDrawArrays(GL_LINES, 0, lineCount * VERTICES_PER_LINE)
        glBindVertexArray(0)
    }
}
