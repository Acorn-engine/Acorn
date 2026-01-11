package dev.acorn.desktop.render.pipelines

import dev.acorn.core.math.Color
import dev.acorn.core.math.Vec2
import dev.acorn.desktop.gl.mesh.QuadMesh
import dev.acorn.desktop.gl.shader.ShaderProgram
import dev.acorn.desktop.gl.shader.ShaderSources
import org.joml.Matrix4f
import org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray
import org.lwjgl.opengl.GL11.GL_LINES
import org.lwjgl.opengl.GL11.glDrawArrays
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.glBufferSubData
import org.lwjgl.opengl.GL20.*
import org.lwjgl.system.MemoryStack

class LinePipeline {
    private val shader = ShaderProgram(ShaderSources.VERT, ShaderSources.FRAG)
    private val mesh = QuadMesh()

    fun draw(proj: Matrix4f, a: Vec2, b: Vec2, color: Color) {
        shader.use()

        MemoryStack.stackPush().use { stack ->
            val pBuf = stack.mallocFloat(16)
            proj.get(pBuf)
            glUniformMatrix4fv(glGetUniformLocation(shader.id, "uProjection"), false, pBuf)
        }

        glUniform4f(glGetUniformLocation(shader.id, "uColor"), color.r, color.g, color.b, color.a)

        val verts = floatArrayOf(a.x, a.y, b.x, b.y)
        glBufferSubData(GL_ARRAY_BUFFER, 0, verts)
        glDrawArrays(GL_LINES, 0, 2)
        glBindVertexArray(0)

        mesh.draw()
    }
}