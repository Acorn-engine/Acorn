package dev.acorn.core.time

import kotlin.math.max

/**
 * Engine owned [Time] implementation, platform updates it once per frame.
 */
class MutableTime(
    private val smootherAlpha: Float = 0.12f, // 0..1, higher = less smoothing
    private val maxDeltaSeconds: Float = 0.05f // clamp to avoid big jumps (50ms)
) : Time {
    override var frame: Long = 0
        private set

    override var nowSeconds: Double = 0.0
        private set

    private var startSeconds: Double = 0.0
    override var sinceStartSeconds: Double = 0.0
        private set

    override var deltaSeconds: Float = 1f / 60f
        private set

    override var rawDeltaSeconds: Float = 1f / 60f
        private set

    private var initialized = false

    private var nextTaskID = 1L
    private val tasks = ArrayList<Scheduled>(64)

    override fun after(seconds: Float, action: () -> Unit): TimeTask =
        schedule(seconds, false, action)

    override fun every(seconds: Float, action: () -> Unit): TimeTask =
        schedule(seconds, true, action)

    private fun schedule(seconds: Float, repeat: Boolean, action: () -> Unit): TimeTask {
        val id = nextTaskID++
        val handle = TimeTask(id)
        tasks += Scheduled(id, max(0f, seconds), repeat, action, nowSeconds.toFloat())
        return handle
    }

    /**
     * Called by the platform once per frame
     */
    fun step(monotonicNowSeconds: Double, rawDtSeconds: Float) {
        if(!initialized) {
            initialized = true
            startSeconds = monotonicNowSeconds
            nowSeconds = monotonicNowSeconds
            sinceStartSeconds = 0.0
            rawDeltaSeconds = 1f / 60f
            deltaSeconds = 1f / 60f
            frame = 0
            return
        }

        frame++
        nowSeconds = monotonicNowSeconds
        sinceStartSeconds = nowSeconds - startSeconds

        val clamped = rawDtSeconds.coerceIn(0f, maxDeltaSeconds)
        rawDeltaSeconds = rawDtSeconds

        deltaSeconds += (clamped - deltaSeconds)
        pumpTasks()
    }

    private fun pumpTasks() {
        if(tasks.isEmpty()) return

        val now = sinceStartSeconds.toFloat()
        var i = 0
        while(i < tasks.size) {
            val t = tasks[i]
            if(t.cancelled) {
                tasks.removeAt(i)
                continue
            }

            if(now - t.lastFire >= t.interval) {
                t.action.invoke()

                if(t.repeat) {
                    t.lastFire = now
                    i++
                } else {
                    tasks.removeAt(i)
                }
            } else {
                i++
            }
        }
    }

    private class Scheduled(
        val id: Long,
        val interval: Float,
        val repeat: Boolean,
        val action: () -> Unit,
        var lastFire: Float
    ) {
        var cancelled: Boolean = false
    }
}