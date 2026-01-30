package dev.acorn.core.input.click

import dev.acorn.core.math.Vec2
import dev.acorn.core.scene.GameObject

sealed class ClickEvent {
    abstract val clickable: Clickable
    abstract val gameObject: GameObject
    abstract val mousePosition: Vec2
    abstract val button: Int
}

data class Click(
    override val clickable: Clickable,
    override val gameObject: GameObject,
    override val mousePosition: Vec2,
    override val button: Int
) : ClickEvent()

data class ClickRelease(
    override val clickable: Clickable,
    override val gameObject: GameObject,
    override val mousePosition: Vec2,
    override val button: Int
) : ClickEvent()

data class ClickEnter(
    override val clickable: Clickable,
    override val gameObject: GameObject,
    override val mousePosition: Vec2,
    override val button: Int = -1
) : ClickEvent()

data class ClickStay(
    override val clickable: Clickable,
    override val gameObject: GameObject,
    override val mousePosition: Vec2,
    override val button: Int = -1
) : ClickEvent()

data class ClickExit(
    override val clickable: Clickable,
    override val gameObject: GameObject,
    override val mousePosition: Vec2,
    override val button: Int = -1
) : ClickEvent()
