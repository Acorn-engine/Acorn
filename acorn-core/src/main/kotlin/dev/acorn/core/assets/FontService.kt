package dev.acorn.core.assets

interface FontService {
    fun load(path: String, size: Float = 32f): FontHandle
}
