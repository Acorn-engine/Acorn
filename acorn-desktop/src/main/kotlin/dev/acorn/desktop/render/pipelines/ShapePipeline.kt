package dev.acorn.desktop.render.pipelines

import dev.acorn.core.math.Color
import dev.acorn.desktop.gl.mesh.QuadMesh
import dev.acorn.desktop.gl.shader.ShaderProgram
import dev.acorn.desktop.gl.shader.ShaderSources
import org.joml.Matrix4f
import org.lwjgl.opengl.GL20.glGetUniformLocation
import org.lwjgl.opengl.GL20.glUniformMatrix4fv
import org.lwjgl.system.MemoryStack

class ShapePipeline {
    private val shader = ShaderProgram(ShaderSources.VERT, ShaderSources.FRAG)
    private val mesh = QuadMesh()

    fun draw(proj: Matrix4f, model: Matrix4f, color: Color, circle: Boolean) {
        shader.use()

        MemoryStack.stackPush().use { stack ->
            val pBuf = stack.mallocFloat(16)
            val mBuf = stack.mallocFloat(16)
            proj.get(pBuf)
            model.get(mBuf)

            glUniformMatrix4fv(glGetUniformLocation(shader.id, "uProjection"), false, pBuf)
            glUniformMatrix4fv(glGetUniformLocation(shader.id, "uModel"), false, mBuf)
        }

        shader.uniform4f("uColor", color.r, color.g, color.b, color.a)
        shader.uniform1i("uUseTexture", 0)
        shader.uniform1i("uMaskType", if(circle) 1 else 0)

        mesh.draw()
    }
}