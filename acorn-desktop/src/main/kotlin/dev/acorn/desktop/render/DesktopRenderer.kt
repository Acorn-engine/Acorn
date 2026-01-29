package dev.acorn.desktop.render

import dev.acorn.core.assets.Sprite
import dev.acorn.core.math.Color
import dev.acorn.core.math.Vec2
import dev.acorn.core.render.Renderer
import dev.acorn.core.render.SpriteMask
import dev.acorn.core.scene.Transform
import dev.acorn.core.window.Window
import dev.acorn.desktop.gl.texture.DesktopTexture
import dev.acorn.desktop.render.pipelines.LineBatchPipeline
import dev.acorn.desktop.render.pipelines.SpriteBatchPipeline
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.system.MemoryUtil

/**
 * Desktop OpenGL renderer implementation using batched rendering.
 *
 * Uses two batched pipelines:
 * - [SpriteBatchPipeline] for textured sprites AND solid shapes (using a 1x1 white texture)
 * - [LineBatchPipeline] for debug lines
 *
 * Switching between quad and line rendering triggers a flush to preserve render order.
 */
class DesktopRenderer : Renderer {
    private val spriteBatch = SpriteBatchPipeline()
    private val lineBatch = LineBatchPipeline()
    private val projection = Matrix4f()

    private var whiteTexture: DesktopTexture? = null

    private enum class BatchMode { NONE, QUADS, LINES }
    private var currentMode = BatchMode.NONE

    /**
     * Lazily creates a 1x1 white pixel texture for rendering shapes.
     */
    private fun getWhiteTexture(): DesktopTexture {
        whiteTexture?.let { return it }

        val texId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, texId)

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

        // Upload 1x1 white pixel
        val pixel = MemoryUtil.memAlloc(4)
        try {
            pixel.put(0xFF.toByte()) // R
            pixel.put(0xFF.toByte()) // G
            pixel.put(0xFF.toByte()) // B
            pixel.put(0xFF.toByte()) // A
            pixel.flip()
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 1, 1, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixel)
        } finally {
            MemoryUtil.memFree(pixel)
        }

        glBindTexture(GL_TEXTURE_2D, 0)

        val tex = DesktopTexture(texId, 1, 1)
        whiteTexture = tex
        return tex
    }

    /**
     * Switches to quad batch mode, flushing lines if necessary.
     */
    private fun switchToQuads() {
        if (currentMode == BatchMode.LINES) {
            lineBatch.flush()
        }
        currentMode = BatchMode.QUADS
    }

    /**
     * Switches to line batch mode, flushing quads if necessary.
     */
    private fun switchToLines() {
        if (currentMode == BatchMode.QUADS) {
            spriteBatch.flush()
        }
        currentMode = BatchMode.LINES
    }

    /**
     * Prepares the renderer for the current frame.
     */
    fun beginFrame(window: Window) {
        val vp = window.viewport
        glViewport(vp.x, vp.y, vp.width, vp.height)
        projection.setOrtho2D(0f, window.virtualWidth.toFloat(), 0f, window.virtualHeight.toFloat())

        spriteBatch.begin(projection)
        lineBatch.begin(projection)
        currentMode = BatchMode.NONE
    }

    /**
     * Ends the current frame, flushing all remaining batched geometry.
     */
    fun endFrame() {
        spriteBatch.end()
        lineBatch.end()
        currentMode = BatchMode.NONE
    }

    /**
     * Clears the current frame buffer with the provided color.
     */
    override fun clear(color: Color) {
        glClearColor(color.r, color.g, color.b, color.a)
        glClear(GL_COLOR_BUFFER_BIT)
    }

    /**
     * Draws a filled axis-aligned rectangle using the sprite batch with a white texture.
     */
    override fun drawRect(transform: Transform, color: Color) {
        switchToQuads()
        spriteBatch.draw(transform, getWhiteTexture(), color, circleMask = false)
    }

    /**
     * Draws a filled circle using the sprite batch with a white texture and circle mask.
     */
    override fun drawCircle(transform: Transform, color: Color) {
        switchToQuads()
        spriteBatch.draw(transform, getWhiteTexture(), color, circleMask = true)
    }

    /**
     * Draws a sprite using its texture and tint, optionally applying a circle mask.
     */
    override fun drawSprite(transform: Transform, sprite: Sprite, mask: SpriteMask) {
        switchToQuads()
        val tex = sprite.texture as DesktopTexture
        spriteBatch.draw(transform, tex, sprite.tint, mask is SpriteMask.Circle)
    }

    /**
     * Draws a debug line between two points.
     */
    override fun drawLine(a: Vec2, b: Vec2, color: Color) {
        switchToLines()
        lineBatch.draw(a, b, color)
    }
}
