package dev.acorn.core.input

import dev.acorn.core.events.EventBus
import dev.acorn.core.events.Subscription

/**
 * Input interface exposed to games through [dev.acorn.core.content.GameContext]
 */
interface Input {
    val events: EventBus<InputEvent>
}

fun Input.onKey(listener: (KeyEvent) -> Unit): Subscription =
    events.subscribe { if(it is KeyEvent) listener(it) }

fun Input.onKey(action: InputAction, listener: (KeyEvent) -> Unit): Subscription =
    events.subscribe { if(it is KeyEvent && it.action == action) listener(it) }

fun Input.onMouseButton(listener: (MouseButtonEvent) -> Unit): Subscription =
    events.subscribe { if(it is MouseButtonEvent) listener(it) }

fun Input.onClick(listener: (MouseButtonEvent) -> Unit): Subscription =
    events.subscribe { if(it is MouseButtonEvent && it.action == InputAction.Press) listener(it) }

fun Input.onCursor(listener: (CursorPosEvent) -> Unit): Subscription =
    events.subscribe { if(it is CursorPosEvent) listener(it) }

fun Input.onScroll(listener: (ScrollEvent) -> Unit): Subscription =
    events.subscribe { if(it is ScrollEvent) listener(it) }

fun Input.onChar(listener: (CharEvent) -> Unit): Subscription =
    events.subscribe { if(it is CharEvent) listener(it) }