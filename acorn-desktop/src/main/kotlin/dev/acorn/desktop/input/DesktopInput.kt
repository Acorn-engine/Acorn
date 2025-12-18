package dev.acorn.desktop.input

import dev.acorn.core.events.EventBus
import dev.acorn.core.input.*
import org.lwjgl.glfw.*
import org.lwjgl.glfw.GLFW.*

/**
 * Desktop input implementation backed by GLFW callbacks
*/
class DesktopInput(private val window: Long) : Input {
    override val events = EventBus<InputEvent>()

    private val maxKeys = 512
    private val keyDown = BooleanArray(maxKeys)
    private val keyPressed = BooleanArray(maxKeys)
    private val keyReleased = BooleanArray(maxKeys)

    private var keyCb: GLFWKeyCallback? = null
    private var mouseCb: GLFWMouseButtonCallback? = null
    private var cursorCb: GLFWCursorPosCallback? = null
    private var scrollCb: GLFWScrollCallback? = null
    private var charCb: GLFWCharCallback? = null

    override fun beginFrame() {
        keyPressed.fill(false)
        keyReleased.fill(false)
    }

    override fun down(key: Int): Boolean =
        key in 0 until maxKeys && keyDown[key]

    override fun pressed(key: Int): Boolean =
        key in 0 until maxKeys && keyPressed[key]

    override fun released(key: Int): Boolean =
        key in 0 until maxKeys && keyReleased[key]

    fun install() {
        keyCb = GLFWKeyCallback.create { _, key, scancode, action, mods ->
            val a = InputAction.fromGlfw(action)
            if(key in 0 until maxKeys) {
                when(a) {
                    InputAction.Press -> {
                        if(!keyDown[key]) keyPressed[key] = true
                        keyDown[key] = true
                    }
                    InputAction.Release -> {
                        keyDown[key] = false
                        keyReleased[key] = true
                    }
                    InputAction.Repeat -> {

                    }
                }
            }

            events.publish(KeyEvent(key, scancode, a, mods))
        }
        glfwSetKeyCallback(window, keyCb)

        mouseCb = GLFWMouseButtonCallback.create { _, button, action, mods ->
            events.publish(MouseButtonEvent(button, InputAction.fromGlfw(action), mods))
        }
        glfwSetMouseButtonCallback(window, mouseCb)

        cursorCb = GLFWCursorPosCallback.create { _, x, y ->
            val w = IntArray(1)
            val h = IntArray(1)
            glfwGetWindowSize(window, w, h)
            val yBottomLeft = (h[0].toDouble() - y).toFloat()
            events.publish(CursorPosEvent(x.toFloat(), yBottomLeft))
        }
        glfwSetCursorPosCallback(window, cursorCb)

        scrollCb = GLFWScrollCallback.create { _, dx, dy ->
            events.publish(ScrollEvent(dx.toFloat(), dy.toFloat()))
        }
        glfwSetScrollCallback(window, scrollCb)

        charCb = GLFWCharCallback.create { _, codepoint ->
            events.publish(CharEvent(codepoint))
        }
        glfwSetCharCallback(window, charCb)
    }

    fun destroy() {
        keyCb?.free()
        mouseCb?.free()
        cursorCb?.free()
        scrollCb?.free()
        charCb?.free()
    }
}