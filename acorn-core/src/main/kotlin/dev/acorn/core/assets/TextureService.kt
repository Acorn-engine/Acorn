package dev.acorn.core.assets

interface TextureService {
    fun load(path: String): TextureHandle
}