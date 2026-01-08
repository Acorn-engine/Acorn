package dev.acorn.core.render

/**
 * Registry of layers for a scene
 */
class RenderLayers {
    private val byName = LinkedHashMap<String, RenderLayer>()

    init {
        define("Background", 0)
        define("Default", 100)
        define("UI", 1000)
    }

    fun define(name: String, index: Int): RenderLayer {
        val layer = RenderLayer(name, index)
        byName[name] = layer
        return layer
    }

    fun get(name: String): RenderLayer = byName[name] ?: define(name, 100)
    fun all(): List<RenderLayer> = byName.values.toList()
}