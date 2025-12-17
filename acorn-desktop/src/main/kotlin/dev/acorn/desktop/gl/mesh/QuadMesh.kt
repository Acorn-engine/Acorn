package dev.acorn.desktop.gl.mesh

import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays

/**
 * A small reusable GPU mesh representing a unit quad centered at the origin
 *
 * The quad is defined in local space as -0.5..0.5 on both axes and includes:
 * - Positon attribute at location 0 (vec2)
 * - UV attribute at location 1 (vec2)
 *
 * This class also creates and owns a VAO, VBO, and IBO
 */
class QuadMesh {
    private val vao: Int
    private val vbo: Int
    private val ibo: Int

    init {
        val verts = floatArrayOf(
            -0.5f, -0.5f,  0f, 0f,
            0.5f, -0.5f,  1f, 0f,
            0.5f,  0.5f,  1f, 1f,
            -0.5f,  0.5f,  0f, 1f
        )

        val idx = intArrayOf(0, 1, 2, 2, 3, 0)

        vao = glGenVertexArrays()
        glBindVertexArray(vao)

        vbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW)

        ibo = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, idx, GL_STATIC_DRAW)

        val stride = 4 * 4
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0L)
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, (2 * 4).toLong())
        glEnableVertexAttribArray(1)

        glBindVertexArray(0)
    }

    /**
     * Draws the quad using indexed rendering
     */
    fun draw() {
        glBindVertexArray(vao)
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0L)
        glBindVertexArray(0)
    }
}