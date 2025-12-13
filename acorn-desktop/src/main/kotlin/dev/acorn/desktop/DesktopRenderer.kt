package dev.acorn.desktop

import dev.acorn.core.assets.Sprite
import dev.acorn.core.math.Color
import dev.acorn.core.render.Renderer
import dev.acorn.core.render.SpriteMask
import dev.acorn.core.scene.Transform
import org.lwjgl.opengl.GL11.*
import kotlin.math.cos
import kotlin.math.sin

class DesktopRenderer : Renderer {
    override fun clear(r: Float, g: Float, b: Float, a: Float) {
        glClearColor(r, g, b, a)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    override fun drawRect(transform: Transform, color: Color) {
        setupDrawing(transform, color)

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
        setupDrawing(transform, color)

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

    override fun drawSprite(transform: Transform, sprite: Sprite, mask: SpriteMask) {
        val tex = sprite.texture as DesktopTexture

        glPushMatrix()
        glTranslatef(transform.position.x, transform.position.y, 0f)
        glRotatef(transform.rotationDeg, 0f, 0f, 1f)
        glScalef(transform.scale.x, transform.scale.y, 1f)
        glEnable(GL_TEXTURE_2D)
        glBindTexture(GL_TEXTURE_2D, tex.id)
        glColor4f(sprite.tint.r, sprite.tint.g, sprite.tint.b, sprite.tint.a)

        glBegin(GL_QUADS)
        glTexCoord2f(0f, 0f); glVertex2f(-0.5f, -0.5f)
        glTexCoord2f(1f, 0f); glVertex2f( 0.5f, -0.5f)
        glTexCoord2f(1f, 1f); glVertex2f( 0.5f,  0.5f)
        glTexCoord2f(0f, 1f); glVertex2f(-0.5f,  0.5f)
        glEnd()

        glDisable(GL_TEXTURE_2D)
        glPopMatrix()
    }

    private fun setupDrawing(transform: Transform, color: Color) {
        glPushMatrix()
        glTranslatef(transform.position.x, transform.position.y, 0f)
        glRotatef(transform.rotationDeg, 0f, 0f, 1f)
        glScalef(transform.scale.x, transform.scale.y, 1f)

        glColor4f(color.r, color.g, color.b, color.a)
    }
}