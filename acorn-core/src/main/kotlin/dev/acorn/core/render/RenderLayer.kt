package dev.acorn.core.render

/**
 * Named layer used for rendering order (e.g. "Background", "Trees", "UI")
 * Layers are ordered by [index] (lower renders first)
 */
data class RenderLayer(
    val name: String,
    val index: Int
)