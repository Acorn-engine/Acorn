package dev.acorn.desktop.render.pipelines

import dev.acorn.core.math.Color
import dev.acorn.core.math.Vec2
import dev.acorn.desktop.gl.mesh.LineMesh
import dev.acorn.desktop.gl.shader.ShaderProgram
import dev.acorn.desktop.gl.shader.ShaderSources
import org.joml.Matrix4f
import org.lwjgl.opengl.GL20.glGetUniformLocation
import org.lwjgl.opengl.GL20.glUniformMatrix4fv
import org.lwjgl.system.MemoryStack

class LinePipeline {
    private val shader = ShaderProgram(ShaderSources.LINE_VERT, ShaderSources.LINE_FRAG)
    private val mesh = LineMesh()

    fun draw(proj: Matrix4f, a: Vec2, b: Vec2, color: Color) {
        shader.use()

        MemoryStack.stackPush().use { stack ->
            val buf = stack.mallocFloat(16)
            proj.get(buf)
            glUniformMatrix4fv(
                glGetUniformLocation(shader.id, "uProjection"),
                false,
                buf
            )
        }

        shader.uniform4f("uColor", color.r, color.g, color.b, color.a)

        mesh.upload(a, b)
        mesh.draw()
    }
}