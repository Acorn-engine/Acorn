package dev.acorn.core.assets

/**
 * Interface for [TextureService] for platforms to extend off of
 */
interface TextureService {
    /**
     * Abstract method to load a texture from a path
     */
    fun load(path: String): TextureHandle
}