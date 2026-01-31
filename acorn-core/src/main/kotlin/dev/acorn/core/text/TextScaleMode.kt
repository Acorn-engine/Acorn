package dev.acorn.core.text

/**
 * Determines how text size is calculated relative to the transform
 */
enum class TextScaleMode {
    /**
     * Text is rendered at the font's pixel size
     */
    FIXED,

    /**
     * Text size scales with the transform's scale property
     */
    SCALE_WITH_TRANSFORM
}