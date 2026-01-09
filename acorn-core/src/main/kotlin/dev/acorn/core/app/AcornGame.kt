package dev.acorn.core.app

import dev.acorn.core.Acorn
import dev.acorn.core.assets.Sprite
import dev.acorn.core.content.GameContext
import dev.acorn.core.content.WindowConfig
import dev.acorn.core.math.Color
import dev.acorn.core.math.Vec2
import dev.acorn.core.render.Renderer
import dev.acorn.core.scene.GameObject
import dev.acorn.core.scene.Scene
import dev.acorn.core.scene.Transform

/**
 * Convenience base class for games built on Acorn
 *
 * This class provides a simple game loop structure that:
 * - Owns a default [Scene] and updates/renders it automatically
 * - Stores the active [GameContext] after setup
 * - Clears the frame with [clearColor] every render call
 */
abstract class AcornGame : Acorn {
    /**
     * Default scene owned by the game
     */
    protected val scene = Scene()

    /**
     * The active game context provided by the platform (desktop, etc.)
     * This is assigned during [setup] and is safe to use afterward
     */
    protected lateinit var context: GameContext

    /**
     * The color used to clear the screen at the beginning of each frame
     * Override to change the default background color
     */
    protected open val clearColor: Color = Color.BLACK

    /**
     * Final setup entry point called by the platform
     * Stores the [GameContext] for later use and delegates to [onStart]
     */
    final override fun setup(context: GameContext) {
        this.context = context
        onStart()
    }

    /**
     * Final update entry point called by hte platform
     * Calls [onUpdate] first, then updates the [scene]
     *
     * @param dt time delta since the last frame in seconds
     */
    final override fun update(dt: Float) {
        onUpdate(dt)
        scene.update(dt)
    }

    /**
     * Final render entry point called by the platform
     * Clears the frame using [clearColor], then calls [onRender], then renders the [scene]
     *
     * @param renderer active renderer for the current platform
     */
    final override fun render(renderer: Renderer) {
        renderer.clear(clearColor)
        onRender(renderer)
        scene.render(renderer)
    }

    /**
     * Allows subclasses to configure the initial window properties
     * Default implementation is a no-op
     *
     * @param config mutable window configuration to edit
     */
    override fun configureWindow(config: WindowConfig) {

    }

    /**
     * Called once after [setup] and after [context] is assigned
     * Use this to load assets and create your initial scene objects
     */
    protected open fun onStart() {}

    /**
     * Called every frame before the [scene] is updated
     * Use this for gameplay logic, input processing, timers, etc
     *
     * @param dt time delta since the last frame in seconds
     */
    protected open fun onUpdate(dt: Float) {}

    /**
     * Called every frame after the screen is cleared but before the [scene] is rendered
     * Use this for custom rendering that should happen before scene objects render
     * (e.g. background effects, debug overlays, custom passes)
     *
     * @param renderer active renderer for the current platform
     */
    protected open fun onRender(renderer: Renderer) {}

    /**
     * Returns the center point of the current window in game coordinates
     */
    protected fun center(): Vec2 =
        Vec2(context.window.virtualWidth / 2f, context.window.virtualHeight / 2f)

    /**
     * Loads a texture from the resources and wraps it as a [Sprite]
     *
     * @param path resource path relative to the resources root (e.g. "player.png")
     * @param tint tint color applied during rendering (multiplied with texture color)
     * @return a new [Sprite] referencing the loaded texture
     */
    protected fun sprite(path: String, tint: Color = Color.WHITE): Sprite =
        Sprite(context.textures.load(path), tint)

    /**
     * Spawns a new [GameObject] into the default [scene]
     *
     * @param transform initial transform for the object
     * @return the newly created [GameObject]
     */
    protected fun spawn(transform: Transform = Transform()): GameObject =
        scene.createGameObject(transform)

    /**
     * Convenience to get context.input
     */
    protected val input get() = context.input

    /**
     * Convenience to get context.time
     */
    protected val time get() = context.time
}