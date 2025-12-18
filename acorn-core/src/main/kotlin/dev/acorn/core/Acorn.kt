package dev.acorn.core

import dev.acorn.core.content.GameContext
import dev.acorn.core.content.WindowConfig
import dev.acorn.core.render.Renderer

/**
 * The main Acorn class, every acorn game will extend from this class and implement the functions
 */
interface Acorn {
    /**
     * Configures the window properties, like the height, width, window title, etc
     *
     * @param config The [WindowConfig]
     */
    fun configureWindow(config: WindowConfig)

    /**
     * Initializes everything in the game, this is where you should create your [dev.acorn.core.scene.GameObject]'s and run things that need to be done during game start.
     *
     * @param context The game context containing the window and textures
     */
    fun setup(context: GameContext)

    /**
     * This function is called every frame by the acorn engine, this is where you should do player movement and general things that need to update
     *
     * @param dtSeconds The current delta seconds (frame independant seconds)
     */
    fun update(dt: Float)

    /**
     * This is where you'll clear the screen and actually render the things that are on the screen onto the window
     *
     * @param renderer The main renderer
     */
    fun render(renderer: Renderer)
}