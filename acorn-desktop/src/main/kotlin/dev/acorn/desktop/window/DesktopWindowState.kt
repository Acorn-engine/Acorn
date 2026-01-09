package dev.acorn.desktop.window

import dev.acorn.core.events.EventBus
import dev.acorn.core.window.FramebufferResized
import dev.acorn.core.window.Window
import dev.acorn.core.window.WindowEvent
import dev.acorn.core.window.WindowResized

class DesktopWindowState(
    initialWidth: Int,
    initialHeight: Int,
) : Window {
    override var width: Int = initialWidth
        private set

    override var height: Int = initialHeight
        private set

    override var framebufferWidth: Int = initialWidth
        private set

    override var framebufferHeight: Int = initialHeight
        private set

    override val events = EventBus<WindowEvent>()

    fun update(windowW: Int, windowH: Int, fbW: Int, fbH: Int) {
        if(windowW != width || windowH != height) {
            width = windowW
            height = windowH
            events.publish(WindowResized(width, height))
        }

        if(fbW != framebufferWidth || fbH != framebufferHeight) {
            framebufferWidth = fbW
            framebufferHeight = fbH
            events.publish(FramebufferResized(framebufferWidth, framebufferHeight))
        }
    }
}