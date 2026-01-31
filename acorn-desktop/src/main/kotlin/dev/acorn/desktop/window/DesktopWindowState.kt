package dev.acorn.desktop.window

import dev.acorn.core.content.WindowScaleMode
import dev.acorn.core.events.EventBus
import dev.acorn.core.window.*
import kotlin.math.min

class DesktopWindowState(
    initialWidth: Int,
    initialHeight: Int,
    override val virtualWidth: Int,
    override val virtualHeight: Int,
    private val scaleMode: WindowScaleMode,
    private val onIconChanged: (String?) -> Unit = {}
) : Window {
    override var width: Int = initialWidth
        private set

    override var height: Int = initialHeight
        private set

    override var framebufferWidth: Int = initialWidth
        private set

    override var framebufferHeight: Int = initialHeight
        private set

    override var viewport: Viewport = Viewport(
        x = 0, y = 0,
        initialWidth, initialHeight,
        initialWidth.toFloat() / virtualWidth,
         initialHeight.toFloat() / virtualHeight
    )
        private set

    override val events = EventBus<WindowEvent>()

    override var icon: String? = "acorn.png"
        set(value) {
            field = value
            onIconChanged(value)
        }

    fun update(windowW: Int, windowH: Int, fbW: Int, fbH: Int) {
        val windowChanged = windowW != width || windowH != height
        val fbChanged = fbW != framebufferWidth || fbH != framebufferHeight

        if(windowChanged) {
            width = windowW
            height = windowH
            events.publish(WindowResized(width, height))
        }

        if(fbChanged) {
            framebufferWidth = fbW
            framebufferHeight = fbH
            events.publish(FramebufferResized(framebufferWidth, framebufferHeight))
        }

        if(windowChanged || fbChanged) {
            viewport = computeViewport(framebufferWidth, framebufferHeight)
            events.publish(ViewportChanged(viewport))
        }
    }

    private fun computeViewport(fbW: Int, fbH: Int): Viewport {
        return when(scaleMode) {
            WindowScaleMode.Stretch -> {
                Viewport(0, 0, fbW, fbH,
                    scaleX = fbW.toFloat() / virtualWidth,
                    scaleY = fbH.toFloat() / virtualHeight
                )
            }

            WindowScaleMode.Fit -> {
                val sx = fbW.toFloat() / virtualWidth.toFloat()
                val sy = fbH.toFloat() / virtualHeight.toFloat()
                val s = min(sx, sy)

                val vpW = (virtualWidth * s).toInt().coerceAtLeast(1)
                val vpH = (virtualHeight * s).toInt().coerceAtLeast(1)
                val vpX = (fbW - vpW) / 2
                val vpY = (fbH - vpH) / 2

                Viewport(vpX, vpY, vpW, vpH, scaleX = s, scaleY = s)
            }
        }
    }
}