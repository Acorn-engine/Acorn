package dev.acorn.desktop.debug

import dev.acorn.core.debug.DebugDraw
import dev.acorn.core.math.Color
import dev.acorn.core.math.Vec2
import dev.acorn.core.render.Renderer
import kotlin.math.cos
import kotlin.math.sin

class DesktopDebugDraw : DebugDraw {
    override var enabled: Boolean = true
    private val cmds = ArrayList<Cmd>(256)

    override fun beginFrame(dtSeconds: Float) {
        if(cmds.isEmpty()) return

        var i = 0
        while(i < cmds.size) {
            val c = cmds[i]
            if (c.ttl != Float.POSITIVE_INFINITY) {
                c.ttl -= dtSeconds
            }

            if (c.ttl != Float.POSITIVE_INFINITY && c.ttl <= 0f) {
                cmds.removeAt(i)
                continue
            }

            i++
        }
    }

    override fun line(a: Vec2, b: Vec2, color: Color, seconds: Float) {
        if(!enabled) return
        cmds += Cmd.Line(a.copy(), b.copy(), color, ttlFor(seconds))
    }

    override fun rect(center: Vec2, size: Vec2, color: Color, seconds: Float) {
        if(!enabled) return
        val hx = size.x * 0.5f
        val hy = size.y * 0.5f

        val bl = Vec2(center.x - hx, center.y - hy)
        val br = Vec2(center.x + hx, center.y - hy)
        val tr = Vec2(center.x + hx, center.y + hy)
        val tl = Vec2(center.x - hx, center.y + hy)

        val ttl = ttlFor(seconds)
        cmds += Cmd.Line(bl, br, color, ttl)
        cmds += Cmd.Line(br, tr, color, ttl)
        cmds += Cmd.Line(tr, tl, color, ttl)
        cmds += Cmd.Line(tl, bl, color, ttl)
    }

    override fun circle(center: Vec2, radius: Float, segments: Int, color: Color, seconds: Float) {
        if(!enabled) return
        val seg = segments.coerceAtLeast(3)
        val step = (Math.PI * 2.0 / seg).toFloat()
        val ttl = ttlFor(seconds)

        var a = 0f
        var prev = Vec2(center.x + cos(a) * radius, center.y + sin(a) * radius)

        for(i in 1..seg) {
            a += step
            val next = Vec2(center.x + cos(a) * radius, center.y + sin(a) * radius)
            cmds += Cmd.Line(prev, next, color, ttl)
            prev = next
        }
    }

    override fun render(renderer: Renderer) {
        if(!enabled || cmds.isEmpty()) return

        for(c in cmds) {
            when(c) {
                is Cmd.Line -> renderer.drawLine(c.a, c.b, c.color)
            }
        }

        var i = 0
        while(i < cmds.size) {
            if(cmds[i].ttl == 0f) {
                cmds.removeAt(i)
            } else {
                i++
            }
        }
    }

    private fun ttlFor(seconds: Float): Float =
        if(seconds <= 0f) Float.POSITIVE_INFINITY else seconds

    private sealed class Cmd(var ttl: Float) {
        class Line(val a: Vec2, val b: Vec2, val color: Color, ttl: Float) : Cmd(ttl)
    }
}