package dev.acorn.core.events

/**
 * Handle returned when subscribing to an EventBus
 */
data class Subscription internal constructor(internal val id: Long)