package dev.acorn.core.assets.font

/**
 * Service for loading fonts from resources of system fonts
 *
 * Fonts can be loaded from:
 * - Resource paths (e.g. "fonts/myfont.ttf")
 * - System fonts (e.g. "Arial")
 */
interface FontService {
    /**
     * Loads a font from a resource path or system font name
     *
     * @param path Resource path to a TTF file or system font name
     * @param size Font size in px
     * @return A [FontHandle] that can be used for text rendering
     * @throws IllegalArgumentException if the font is not found
     */
    fun load(path: String, size: Float): FontHandle

    /**
     * Returns the default fault (Arial, 12px)
     */
    fun default(): FontHandle
}