package dev.acorn.core.input.click

import dev.acorn.core.events.EventBus
import dev.acorn.core.events.Subscription
import dev.acorn.core.input.CursorPosEvent
import dev.acorn.core.input.Input
import dev.acorn.core.input.InputAction
import dev.acorn.core.input.MouseButtonEvent
import dev.acorn.core.math.Vec2
import dev.acorn.core.scene.GameObject
import dev.acorn.core.window.Window

class ClickSystem {
    val events = EventBus<ClickEvent>()

    private var mousePos = Vec2(0f, 0f)
    private val hoveredIds = HashSet<Int>()

    private var cursorSub: Subscription? = null
    private var mouseSub: Subscription? = null

    private var input: Input? = null
    private var window: Window? = null

    fun initialize(input: Input, window: Window) {
        this.input = input
        this.window = window

        cursorSub = input.events.subscribe { event ->
            if (event is CursorPosEvent) {
                mousePos = screenToWorld(event.x, event.y)
            }
        }

        mouseSub = input.events.subscribe { event ->
            if (event is MouseButtonEvent) {
                pendingMouseEvents.add(event)
            }
        }
    }

    private val pendingMouseEvents = mutableListOf<MouseButtonEvent>()

    fun step(objects: List<GameObject>) {
        val clickables = ArrayList<Pair<GameObject, Clickable>>()
        for (go in objects) {
            for (c in go.getComponents(Clickable::class.java)) {
                if (c.enabled) clickables += go to c
            }
        }

        val nextHovered = HashSet<Int>()
        for ((go, clickable) in clickables) {
            if (clickable.bounds.containsPoint(mousePos, go)) {
                nextHovered += go.id

                val wasHovered = go.id in hoveredIds
                if (!wasHovered) {
                    publishEnter(go, clickable)
                } else {
                    publishStay(go, clickable)
                }
            }
        }

        for (id in hoveredIds) {
            if (id !in nextHovered) {
                val pair = clickables.find { it.first.id == id }
                if (pair != null) {
                    publishExit(pair.first, pair.second)
                }
            }
        }

        hoveredIds.clear()
        hoveredIds.addAll(nextHovered)

        for (event in pendingMouseEvents) {
            for ((go, clickable) in clickables) {
                if (go.id in hoveredIds) {
                    if (event.action == InputAction.Press) {
                        publishClick(go, clickable, event.button)
                    } else if (event.action == InputAction.Release) {
                        publishRelease(go, clickable, event.button)
                    }
                }
            }
        }
        pendingMouseEvents.clear()
    }

    fun destroy() {
        cursorSub?.let { input?.events?.unsubscribe(it) }
        mouseSub?.let { input?.events?.unsubscribe(it) }
        cursorSub = null
        mouseSub = null
        input = null
        window = null
    }

    private fun screenToWorld(screenX: Float, screenY: Float): Vec2 {
        val w = window ?: return Vec2(screenX, screenY)

        val fbScaleX = w.framebufferWidth.toFloat() / w.width.toFloat()
        val fbScaleY = w.framebufferHeight.toFloat() / w.height.toFloat()
        val fbX = screenX * fbScaleX
        val fbY = screenY * fbScaleY

        val vp = w.viewport
        val localX = fbX - vp.x
        val localY = fbY - vp.y

        val worldX = localX / vp.scaleX
        val worldY = localY / vp.scaleY

        return Vec2(worldX, worldY)
    }

    private fun publishEnter(go: GameObject, clickable: Clickable) {
        val e = ClickEnter(clickable, go, Vec2(mousePos.x, mousePos.y))
        events.publish(e)
        clickable.events.publish(e)
    }

    private fun publishStay(go: GameObject, clickable: Clickable) {
        val e = ClickStay(clickable, go, Vec2(mousePos.x, mousePos.y))
        events.publish(e)
        clickable.events.publish(e)
    }

    private fun publishExit(go: GameObject, clickable: Clickable) {
        val e = ClickExit(clickable, go, Vec2(mousePos.x, mousePos.y))
        events.publish(e)
        clickable.events.publish(e)
    }

    private fun publishClick(go: GameObject, clickable: Clickable, button: Int) {
        val e = Click(clickable, go, Vec2(mousePos.x, mousePos.y), button)
        events.publish(e)
        clickable.events.publish(e)
    }

    private fun publishRelease(go: GameObject, clickable: Clickable, button: Int) {
        val e = ClickRelease(clickable, go, Vec2(mousePos.x, mousePos.y), button)
        events.publish(e)
        clickable.events.publish(e)
    }
}
