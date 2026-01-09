package dev.acorn.desktop.render

import dev.acorn.core.assets.Sprite
import dev.acorn.core.math.Color
import dev.acorn.core.render.Renderer
import dev.acorn.core.render.SpriteMask
import dev.acorn.core.scene.Transform
import dev.acorn.core.window.Window
import dev.acorn.desktop.gl.texture.DesktopTexture
import dev.acorn.desktop.render.pipelines.ShapePipeline
import dev.acorn.desktop.render.pipelines.SpritePipeline
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11.*

/**
 * Desktop OpenGL renderer implementation
 *
 * Uses 2D orthographic projection and two pipelines:
 * - [ShapePipeline] for solid colored rects/circles
 * - [SpritePipeline] for textured sprites
 */
class DesktopRenderer : Renderer {
    private val shapePipeline = ShapePipeline()
    private val spritePipeline = SpritePipeline()
    private val projection = Matrix4f()

    /**
     * Prepares the renderer for the current frame
     *
     * @param window The current window
    */
    fun beginFrame(window: Window) {
        val vp = window.viewport
        glViewport(vp.x, vp.y, vp.width, vp.height)
        projection.setOrtho2D(0f, window.virtualWidth.toFloat(), 0f, window.virtualHeight.toFloat())
    }

    /**
     * Clears the current frame buffer with the provided color
     *
     * @param color RGBA clear color
     */
    override fun clear(color: Color) {
        glClearColor(color.r, color.g, color.b, color.a)
        glClear(GL_COLOR_BUFFER_BIT)
    }

    /**
     * Draws a filled axis-aligned rectangle using the object's model transform
     */
    override fun drawRect(transform: Transform, color: Color) {
        shapePipeline.draw(projection, modelFrom(transform), color, false)
    }

    /**
     * Draws a filled circle by rendering a quad and applying a circle mask in the fragment shader
     */
    override fun drawCircle(transform: Transform, color: Color) {
        shapePipeline.draw(projection, modelFrom(transform), color, true)
    }

    /**
     * Draws a sprite using its texture and tint, optionally applying a mask
     */
    override fun drawSprite(transform: Transform, sprite: Sprite, mask: SpriteMask) {
        val tex = sprite.texture as DesktopTexture

        spritePipeline.draw(projection, modelFrom(transform), tex, sprite.tint, mask is SpriteMask.Circle)
    }

    /**
     * Builds a model matrix from a 2D transform
     */
    private fun modelFrom(t: Transform): Matrix4f =
        Matrix4f()
            .translate(t.position.x, t.position.y, 0f)
            .rotateZ(Math.toRadians(t.rotationDeg.toDouble()).toFloat())
            .scale(t.scale.x, t.scale.y, 1f)
}