package dev.acorn.core.debug

import dev.acorn.core.math.Color
import dev.acorn.core.math.Vec2
import dev.acorn.core.render.Renderer

/**
 * Engine debug drawing facility (gizmos)
 *
 * Lifetimes:
 * - seconds <= 0f: drawn for one frame
 * - seconds  > 0f: persists for that duration
 */
interface DebugDraw {
    var enabled: Boolean

    fun beginFrame(dtSeconds: Float)
    fun line(a: Vec2, b: Vec2, color: Color = Color.GREEN, seconds: Float = 0f)
    fun rect(center: Vec2, size: Vec2, color: Color = Color.GREEN, seconds: Float = 0f)
    fun circle(center: Vec2, radius: Float, segments: Int = 24, color: Color = Color.GREEN, seconds: Float = 0f)

    fun render(renderer: Renderer)
}