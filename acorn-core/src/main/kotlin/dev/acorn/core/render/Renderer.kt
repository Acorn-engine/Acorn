package dev.acorn.core.render

import dev.acorn.core.assets.Sprite
import dev.acorn.core.assets.font.Font
import dev.acorn.core.math.Color
import dev.acorn.core.math.Vec2
import dev.acorn.core.scene.Transform
import dev.acorn.core.text.TextAlign

/**
 * The main renderer for Acorn, this handles the drawing of shapes, drawing of sprites, clearing the screen, and eventually more
 * Everything in here should be overwritten by the acorn platform, such as desktop, ios, android, etc
 */
interface Renderer {
    /**
     * Clears the screen
     *
     * @param color Color to clear the screen with
     */
    fun clear(color: Color)

    /**
     * Draws a rectangle at a given point
     *
     * @param transform The transform of the new rect
     * @param color The color of the new rect
     */
    fun drawRect(transform: Transform, color: Color)

    /**
     * Draws a circle at a given point
     *
     * @param transform The transform of the new circle
     * @param color The color of the new circle
     */
    fun drawCircle(transform: Transform, color: Color)

    /**
     * Draws a sprite at a given point
     *
     * @param transform The transform of the new sprite
     * @param sprite The sprite to draw
     * @param mask The sprite mask
     */
    fun drawSprite(transform: Transform, sprite: Sprite, mask: SpriteMask)

    /**
     * Draws a single 2D line segment
     */
    fun drawLine(a: Vec2, b: Vec2, color: Color)

    /**
     * Draws text at a given position
     *
     * @param text The text string to render
     * @param transform The transform for position, rotation, and scale
     * @param font The font to use for rendering
     * @param color Color of the text
     * @param align Horizontal text alignment relative to the transform position
     */
    fun drawText(text: String, transform: Transform, font: Font, color: Color, align: TextAlign = TextAlign.LEFT)
}