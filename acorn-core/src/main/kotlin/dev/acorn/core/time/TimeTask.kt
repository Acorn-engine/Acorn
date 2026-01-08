package dev.acorn.core.time

/**
 * Handle for a scheduled time task
 */
class TimeTask internal constructor(internal val id: Long) {
    var cancelled: Boolean = false
        private set

    fun cancel() {
        cancelled = true
    }
}