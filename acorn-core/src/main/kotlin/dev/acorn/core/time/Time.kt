package dev.acorn.core.time

/**
 * Engine managed time service exposed to games via [dev.acorn.core.content.GameContext]
 *
 * - [nowSeconds] is monotonically increasing
 * - [deltaSeconds] is a stabilized/clamped delta
 * - [rawDeltaSeconds] is the raw measured delta
 */
interface Time {
    val frame: Long

    val nowSeconds: Double
    val sinceStartSeconds: Double

    val deltaSeconds: Float
    val rawDeltaSeconds: Float

    fun after(seconds: Float, action: () -> Unit): TimeTask
    fun every(seconds: Float, action: () -> Unit): TimeTask
}