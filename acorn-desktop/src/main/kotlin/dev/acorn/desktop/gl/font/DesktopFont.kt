package dev.acorn.desktop.gl.font

import dev.acorn.core.assets.FontHandle
import dev.acorn.desktop.gl.texture.DesktopTexture
import org.lwjgl.stb.STBTTPackedchar

/**
 * Desktop OpenGL font implementation.
 *
 * Holds the baked glyph atlas texture and the packed-char metrics buffer
 * covering ASCII 32–126 (95 characters).
 *
 * @property atlasTexture GPU texture containing the grayscale glyph atlas (as RGBA)
 * @property packedChars Native buffer of per-glyph metrics produced by stbtt_PackFontRange
 * @property atlasWidth Width of the atlas in pixels
 * @property atlasHeight Height of the atlas in pixels
 */
class DesktopFont(
    val atlasTexture: DesktopTexture,
    val packedChars: STBTTPackedchar.Buffer,
    val atlasWidth: Int,
    val atlasHeight: Int
) : FontHandle
