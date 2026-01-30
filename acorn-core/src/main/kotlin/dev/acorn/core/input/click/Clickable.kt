package dev.acorn.core.input.click

import dev.acorn.core.events.EventBus
import dev.acorn.core.events.Subscription
import dev.acorn.core.scene.Component

class Clickable(var bounds: ClickBounds = ColliderClickBounds()) : Component() {
    var enabled: Boolean = true
    val events: EventBus<ClickEvent> = EventBus()

    fun onClick(listener: (Click) -> Unit): Subscription =
        events.subscribe { if (it is Click) listener(it) }

    fun onRelease(listener: (ClickRelease) -> Unit): Subscription =
        events.subscribe { if (it is ClickRelease) listener(it) }

    fun onEnter(listener: (ClickEnter) -> Unit): Subscription =
        events.subscribe { if (it is ClickEnter) listener(it) }

    fun onStay(listener: (ClickStay) -> Unit): Subscription =
        events.subscribe { if (it is ClickStay) listener(it) }

    fun onExit(listener: (ClickExit) -> Unit): Subscription =
        events.subscribe { if (it is ClickExit) listener(it) }

    override fun onRemoved() {
        events.clear()
    }
}
