package dev.acorn.desktop.render

import dev.acorn.core.assets.Font
import dev.acorn.core.assets.Sprite
import dev.acorn.core.math.Color
import dev.acorn.core.math.Vec2
import dev.acorn.core.render.Renderer
import dev.acorn.core.render.SpriteMask
import dev.acorn.core.render.TextAlign
import dev.acorn.core.scene.Transform
import dev.acorn.core.window.Window
import dev.acorn.desktop.gl.font.DesktopFont
import dev.acorn.desktop.gl.texture.DesktopTexture
import dev.acorn.desktop.render.pipelines.LineBatchPipeline
import dev.acorn.desktop.render.pipelines.SpriteBatchPipeline
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.stb.STBTTAlignedQuad
import org.lwjgl.stb.STBTruetype.stbtt_GetPackedQuad
import org.lwjgl.system.MemoryStack
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

    /**
     * Draws a string of text using STB TrueType glyph metrics.
     *
     * Each character is submitted as a quad through the sprite batch. All
     * glyphs for the same font share one atlas texture, so no extra flushes
     * occur mid-string.
     *
     * The atlas is stored without a vertical flip (STB row 0 = top), so V
     * coordinates are flipped here to match the GL convention.
     */
    override fun drawText(transform: Transform, text: String, font: Font, color: Color, align: TextAlign) {
        if (text.isEmpty()) return

        switchToQuads()

        val df = font.handle as DesktopFont
        val scaleX = transform.scale.x
        val scaleY = transform.scale.y
        val baselineY = transform.position.y

        MemoryStack.stackPush().use { stack ->
            val q = STBTTAlignedQuad.malloc(stack)
            val xp = stack.floats(0f)
            val yp = stack.floats(0f)

            // Compute total string width for CENTER / RIGHT alignment
            val startX: Float
            if (align != TextAlign.LEFT) {
                var totalWidth = 0f
                xp.put(0, 0f)
                yp.put(0, 0f)
                for (ch in text) {
                    val codepoint = ch.code
                    if (codepoint < 32 || codepoint > 126) continue
                    stbtt_GetPackedQuad(
                        df.packedChars,
                        df.atlasWidth, df.atlasHeight,
                        codepoint - 32,
                        xp, yp, q, false
                    )
                }
                totalWidth = xp.get(0) * scaleX
                startX = when (align) {
                    TextAlign.CENTER -> transform.position.x - totalWidth / 2f
                    TextAlign.RIGHT  -> transform.position.x - totalWidth
                    else             -> transform.position.x
                }
            } else {
                startX = transform.position.x
            }

            // Render pass
            xp.put(0, 0f)
            yp.put(0, 0f)

            for (ch in text) {
                val codepoint = ch.code
                if (codepoint < 32 || codepoint > 126) continue

                stbtt_GetPackedQuad(
                    df.packedChars,
                    df.atlasWidth, df.atlasHeight,
                    codepoint - 32,
                    xp, yp, q, false
                )

                // Convert STB quad to world space
                // STB Y increases downward; our world Y increases upward
                val wx0 = startX + q.x0() * scaleX
                val wx1 = startX + q.x1() * scaleX
                val wy0 = baselineY - q.y1() * scaleY  // bottom in world space
                val wy1 = baselineY - q.y0() * scaleY  // top in world space

                val u0 = q.s0()
                val u1 = q.s1()
                // No V flip: atlas uploaded without flip so GL V = STB t directly.
                // t1 > t0 (bottom of glyph in atlas), so bottom quad vertex uses t1.
                val v0 = q.t1()  // bottom GL V (glyph bottom edge)
                val v1 = q.t0()  // top GL V (glyph top edge)

                spriteBatch.drawGlyph(wx0, wy0, wx1, wy1, u0, v0, u1, v1, df.atlasTexture, color)
            }
        }
    }
}
