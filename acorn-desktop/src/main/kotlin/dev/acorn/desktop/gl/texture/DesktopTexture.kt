package dev.acorn.desktop.gl.texture

import dev.acorn.core.assets.TextureHandle

/**
 * Desktop OpenGL texture implementation
 * Wraps an OpenGL texture object ID along with its dimensions
 *
 * @property id OpenGL texture object ID (glGenTextures)
 * @property width Texture width in pixels
 * @property height Texture height in pixels
 */
class DesktopTexture(val id: Int, val width: Int, val height: Int): TextureHandle