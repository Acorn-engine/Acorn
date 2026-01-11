package dev.acorn.desktop.gl.mesh

import dev.acorn.core.math.Vec2
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays

class LineMesh {
    private val vao: Int
    private val vbo: Int

    init {
        vao = glGenVertexArrays()
        vbo = glGenBuffers()

        glBindVertexArray(vao)
        glBindBuffer(GL_ARRAY_BUFFER, vbo)

        glBufferData(GL_ARRAY_BUFFER, 4 * 4, GL_DYNAMIC_DRAW)

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * 4, 0L)
        glEnableVertexAttribArray(0)

        glBindVertexArray(0)
    }

    fun upload(a: Vec2, b: Vec2) {
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferSubData(
            GL_ARRAY_BUFFER,
            0,
            floatArrayOf(a.x, a.y, b.x, b.y)
        )
    }

    fun draw() {
        glBindVertexArray(vao)
        glDrawArrays(GL_LINES, 0, 2)
        glBindVertexArray(0)
    }
}