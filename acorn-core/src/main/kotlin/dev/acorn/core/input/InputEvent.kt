package dev.acorn.core.input

/**
 * Interface for all input events
 */
sealed interface InputEvent

data class KeyEvent(
    val key: Int,
    val scancode: Int,
    val action: InputAction,
    val mods: Int
) : InputEvent

data class MouseButtonEvent(
    val button: Int,
    val action: InputAction,
    val mods: Int
) : InputEvent {
    val value: Int get() = if(action == InputAction.Release) 0 else 1
}

data class CursorPosEvent(
    val x: Float,
    val y: Float
) : InputEvent

data class ScrollEvent(
    val dx: Float,
    val dy: Float
) : InputEvent

data class CharEvent(
    val codepoint: Int
) : InputEvent