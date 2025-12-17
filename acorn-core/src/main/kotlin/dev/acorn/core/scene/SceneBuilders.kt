package dev.acorn.core.scene

import dev.acorn.core.assets.Sprite
import dev.acorn.core.components.SpriteRenderer
import dev.acorn.core.math.Vec2

/**
 * Extension for spawning a sprite rendered [GameObject] into a [Scene]
 * This creates a new [GameObject] with a [Transform] and a [SpriteRenderer] component
 *
 * @param sprite sprite to render
 * @param position world position (in game coordinates)
 * @param size desired size of the sprite quad (in game coordinates)
 * @return the newly created and configured [GameObject]
 */
fun Scene.spriteObject(sprite: Sprite, position: Vec2, size: Vec2): GameObject {
    return createGameObject(
        Transform(position = position, scale = size)
    ).apply {
        addComponent(SpriteRenderer(sprite))
    }
}