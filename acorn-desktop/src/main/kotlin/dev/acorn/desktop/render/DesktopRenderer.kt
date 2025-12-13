package dev.acorn.desktop.render

import dev.acorn.core.assets.Sprite
import dev.acorn.core.math.Color
import dev.acorn.core.render.Renderer
import dev.acorn.core.render.SpriteMask
import dev.acorn.core.scene.Transform
import dev.acorn.desktop.gl.texture.DesktopTexture
import dev.acorn.desktop.render.pipelines.ShapePipeline
import dev.acorn.desktop.render.pipelines.SpritePipeline
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11.*

class DesktopRenderer : Renderer {
    private val shapePipeline = ShapePipeline()
    private val spritePipeline = SpritePipeline()
    private val projection = Matrix4f()

    fun beginFrame(windowW: Int, windowH: Int, framebufferW: Int, framebufferH: Int) {
        glViewport(0, 0, framebufferW, framebufferH)
        projection.setOrtho2D(0f, windowW.toFloat(), 0f, windowH.toFloat())
    }

    override fun clear(color: Color) {
        glClearColor(color.r, color.g, color.b, color.a)
        glClear(GL_COLOR_BUFFER_BIT)
    }

    override fun drawRect(transform: Transform, color: Color) {
        shapePipeline.draw(projection, modelFrom(transform), color, false)
    }

    override fun drawCircle(transform: Transform, color: Color, segments: Int) {
        shapePipeline.draw(projection, modelFrom(transform), color, true)
    }

    override fun drawSprite(transform: Transform, sprite: Sprite, mask: SpriteMask) {
        val tex = sprite.texture as DesktopTexture

        spritePipeline.draw(projection, modelFrom(transform), tex, sprite.tint, mask is SpriteMask.Circle)
    }

    private fun modelFrom(t: Transform): Matrix4f =
        Matrix4f()
            .translate(t.position.x, t.position.y, 0f)
            .rotateZ(Math.toRadians(t.rotationDeg.toDouble()).toFloat())
            .scale(t.scale.x, t.scale.y, 1f)
}