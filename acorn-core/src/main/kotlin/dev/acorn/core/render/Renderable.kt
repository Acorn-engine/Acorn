package dev.acorn.core.render

/**
 * Interface for [dev.acorn.core.scene.Component]'s that want the [dev.acorn.core.scene.Scene] to render them in a sorted order
 */
interface Renderable {
    val layer: String
    val orderInLayer: Int
}