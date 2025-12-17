package dev.acorn.desktop.render.pipelines

import dev.acorn.core.math.Color
import dev.acorn.desktop.gl.mesh.QuadMesh
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

/**
 * Rendering pipeline for textured sprites
 * It uses a shared quad mesh and a shader that multiplies a tint color by the sampled texture color, with an optional circle mask
 */
class SpritePipeline {
    private val shader = ShaderProgram(ShaderSources.VERT, ShaderSources.FRAG)
    private val mesh = QuadMesh()

    /**
     * Draws a textured sprite
     *
     * @param proj 2D projection matrix (typically ortho)
     * @param model Model matrix (translation/rotation/scale)
     * @param tex OpenGL texture to sample
     * @param tint RGBA color multiplied with the sampled texture color
     * @param circleMask If true applies a circular mask in the fragment shader
     */
    fun draw(proj: Matrix4f, model: Matrix4f, tex: DesktopTexture, tint: Color, circleMask: Boolean) {
        shader.use()

        MemoryStack.stackPush().use { stack ->
            val pBuf = stack.mallocFloat(16)
            val mBuf = stack.mallocFloat(16)
            proj.get(pBuf)
            model.get(mBuf)

            glUniformMatrix4fv(glGetUniformLocation(shader.id, "uProjection"), false, pBuf)
            glUniformMatrix4fv(glGetUniformLocation(shader.id, "uModel"), false, mBuf)
        }

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, tex.id)
        shader.uniform1i("uTex", 0)

        shader.uniform4f("uColor", tint.r, tint.g, tint.b, tint.a)
        shader.uniform1i("uUseTexture", 1)
        shader.uniform1i("uMaskType", if (circleMask) 1 else 0)

        mesh.draw()

        glBindTexture(GL_TEXTURE_2D, 0)
    }
}