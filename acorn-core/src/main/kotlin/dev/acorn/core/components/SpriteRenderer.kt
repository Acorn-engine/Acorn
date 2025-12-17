package dev.acorn.core.components

import dev.acorn.core.assets.Sprite
import dev.acorn.core.render.Renderer
import dev.acorn.core.render.SpriteMask
import dev.acorn.core.scene.Component

/**
 * Renders sprites onto the [Renderer]
 *
 * @param sprite The sprite
 */
class SpriteRenderer(var sprite: Sprite): Component() {
    /**
     * Renders sprites onto the [Renderer] and applies [SpriteMask]'s
     *
     * @param renderer The main [Renderer] of the game
     */
    override fun render(renderer: Renderer) {
        val shape = gameObject.getComponent(SpriteShapeRenderer::class.java)
        val mask = if(shape?.shape == ShapeType.CIRCLE) SpriteMask.Circle else SpriteMask.None
        renderer.drawSprite(gameObject.transform, sprite, mask)
    }
}