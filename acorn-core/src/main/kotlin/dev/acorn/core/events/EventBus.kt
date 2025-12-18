package dev.acorn.core.events

/**
 * Synchronous event bus
 * Events are delivered in subscription order
 */
class EventBus<T: Any> {
    private val listeners = LinkedHashMap<Long, (T) -> Unit>()
    private var nextID = 1L

    fun subscribe(listener: (T) -> Unit): Subscription {
        val id = nextID++
        listeners[id] = listener
        return Subscription(id)
    }

    fun unsubscribe(subscription: Subscription) {
        listeners.remove(subscription.id)
    }

    fun publish(event: T) {
        listeners.values.forEach { it(event) }
    }

    fun clear() {
        listeners.clear()
    }
}