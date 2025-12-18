package dev.acorn.desktop.input

import dev.acorn.core.events.EventBus
import dev.acorn.core.input.*
import org.lwjgl.glfw.*
import org.lwjgl.glfw.GLFW.*

/**
 * Handles the listeners and callbacks for desktop related input
 */
class DesktopInput(private val window: Long): Input {
    override val events = EventBus<InputEvent>()

    private var keyCb: GLFWKeyCallback? = null
    private var mouseCb: GLFWMouseButtonCallback? = null
    private var cursorCb: GLFWCursorPosCallback? = null
    private var scrollCb: GLFWScrollCallback? = null
    private var charCb: GLFWCharCallback? = null

    /**
     * Set up the desktop specific input for GLFW
     */
    fun install() {
        keyCb = GLFWKeyCallback.create { _, key, scancode, action, mods ->
            events.publish(KeyEvent(key, scancode, InputAction.fromGlfw(action), mods))
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

    /**
     * Should be called when you want to freeze input, or when the game is quit
     */
    fun destroy() {
        keyCb?.free()
        mouseCb?.free()
        cursorCb?.free()
        scrollCb?.free()
        charCb?.free()
    }
}