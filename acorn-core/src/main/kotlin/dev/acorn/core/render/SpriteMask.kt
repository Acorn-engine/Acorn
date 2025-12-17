package dev.acorn.core.render

sealed class SpriteMask {
    data object None : SpriteMask()
    data object Circle : SpriteMask()
}