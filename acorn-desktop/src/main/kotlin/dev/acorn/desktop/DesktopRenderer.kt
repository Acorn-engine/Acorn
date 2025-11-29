package dev.acorn.desktop

import dev.acorn.core.Color
import dev.acorn.core.Renderer
import dev.acorn.core.gameobject.Transform
import org.lwjgl.opengl.GL11.*
import kotlin.math.cos
import kotlin.math.sin

class DesktopRenderer : Renderer {
    override fun clear(r: Float, g: Float, b: Float, a: Float) {
        glClearColor(r, g, b, a)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    override fun drawRect(transform: Transform, color: Color) {
        glPushMatrix()
        glTranslatef(transform.position.x, transform.position.y, 0f)
        glRotatef(transform.rotationDeg, 0f, 0f, 1f)
        glScalef(transform.scale.x, transform.scale.y, 1f)

        glColor4f(color.r, color.g, color.b, color.a)

        glBegin(GL_TRIANGLES)
        glVertex2f(-0.5f, -0.5f)
        glVertex2f( 0.5f, -0.5f)
        glVertex2f( 0.5f,  0.5f)

        glVertex2f(-0.5f, -0.5f)
        glVertex2f( 0.5f,  0.5f)
        glVertex2f(-0.5f,  0.5f)
        glEnd()

        glPopMatrix()
    }

    override fun drawCircle(transform: Transform, color: Color, segments: Int) {
        glPushMatrix()
        glTranslatef(transform.position.x, transform.position.y, 0f)
        glRotatef(transform.rotationDeg, 0f, 0f, 1f)
        glScalef(transform.scale.x, transform.scale.y, 1f)

        glColor4f(color.r, color.g, color.b, color.a)

        glBegin(GL_TRIANGLE_FAN)
        glVertex2f(0f, 0f)

        val step = (Math.PI * 2.0 / segments).toFloat()
        var angle = 0f
        for(i in 0..segments) {
            val x = cos(angle)
            val y = sin(angle)
            glVertex2f(x.toFloat() * 0.5f, y.toFloat() * 0.5f)
            angle += step
        }

        glEnd()
        glPopMatrix()
    }
}