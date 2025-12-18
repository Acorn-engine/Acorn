package dev.acorn.core.input

import dev.acorn.core.events.EventBus
import dev.acorn.core.events.Subscription
import dev.acorn.core.math.Vec2

/**
 * Input interface exposed to games through [dev.acorn.core.content.GameContext]
 */
interface Input {
    val events: EventBus<InputEvent>

    fun beginFrame() {}

    fun down(key: Int): Boolean
    fun pressed(key: Int): Boolean
    fun released(key: Int): Boolean

    fun axis(negativeKey: Int, positiveKey: Int): Float {
        var v = 0f
        if(down(negativeKey)) v -= 1f
        if(down(positiveKey)) v += 1f
        return v
    }

    fun axis2D(left: Int, right: Int, downKey: Int, upKey: Int): Vec2 =
        Vec2(axis(left, right), axis(downKey, upKey))
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