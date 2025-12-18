package dev.acorn.core.input

enum class InputAction(val value: Int) {
    Release(0),
    Press(1),
    Repeat(2);

    companion object {
        fun fromGlfw(action: Int): InputAction =
            when(action) {
                0 -> Release
                1 -> Press
                2 -> Repeat
                else -> throw IllegalArgumentException("Unknown action $action")
            }
    }
}