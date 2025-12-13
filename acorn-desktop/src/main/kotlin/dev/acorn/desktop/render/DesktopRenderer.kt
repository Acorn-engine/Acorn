package dev.acorn.desktop.render

import dev.acorn.core.assets.Sprite
import dev.acorn.core.math.Color
import dev.acorn.core.render.Renderer
import dev.acorn.core.render.SpriteMask
import dev.acorn.core.scene.Transform
import dev.acorn.desktop.gl.texture.DesktopTexture
import org.lwjgl.opengl.GL11
import kotlin.math.cos
import kotlin.math.sin

class DesktopRenderer : Renderer {
    override fun clear(r: Float, g: Float, b: Float, a: Float) {
        GL11.glClearColor(r, g, b, a)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
    }

    override fun drawRect(transform: Transform, color: Color) {
        setupDrawing(transform, color)

        GL11.glBegin(GL11.GL_TRIANGLES)
        GL11.glVertex2f(-0.5f, -0.5f)
        GL11.glVertex2f(0.5f, -0.5f)
        GL11.glVertex2f(0.5f, 0.5f)

        GL11.glVertex2f(-0.5f, -0.5f)
        GL11.glVertex2f(0.5f, 0.5f)
        GL11.glVertex2f(-0.5f, 0.5f)
        GL11.glEnd()

        GL11.glPopMatrix()
    }

    override fun drawCircle(transform: Transform, color: Color, segments: Int) {
        setupDrawing(transform, color)

        GL11.glBegin(GL11.GL_TRIANGLE_FAN)
        GL11.glVertex2f(0f, 0f)

        val step = (Math.PI * 2.0 / segments).toFloat()
        var angle = 0f
        for(i in 0..segments) {
            val x = cos(angle)
            val y = sin(angle)
            GL11.glVertex2f(x.toFloat() * 0.5f, y.toFloat() * 0.5f)
            angle += step
        }

        GL11.glEnd()
        GL11.glPopMatrix()
    }

    override fun drawSprite(transform: Transform, sprite: Sprite, mask: SpriteMask) {
        val tex = sprite.texture as DesktopTexture

        GL11.glPushMatrix()
        GL11.glTranslatef(transform.position.x, transform.position.y, 0f)
        GL11.glRotatef(transform.rotationDeg, 0f, 0f, 1f)
        GL11.glScalef(transform.scale.x, transform.scale.y, 1f)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.id)
        GL11.glColor4f(sprite.tint.r, sprite.tint.g, sprite.tint.b, sprite.tint.a)

        GL11.glBegin(GL11.GL_QUADS)
        GL11.glTexCoord2f(0f, 0f); GL11.glVertex2f(-0.5f, -0.5f)
        GL11.glTexCoord2f(1f, 0f); GL11.glVertex2f(0.5f, -0.5f)
        GL11.glTexCoord2f(1f, 1f); GL11.glVertex2f(0.5f, 0.5f)
        GL11.glTexCoord2f(0f, 1f); GL11.glVertex2f(-0.5f, 0.5f)
        GL11.glEnd()

        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glPopMatrix()
    }

    private fun setupDrawing(transform: Transform, color: Color) {
        GL11.glPushMatrix()
        GL11.glTranslatef(transform.position.x, transform.position.y, 0f)
        GL11.glRotatef(transform.rotationDeg, 0f, 0f, 1f)
        GL11.glScalef(transform.scale.x, transform.scale.y, 1f)

        GL11.glColor4f(color.r, color.g, color.b, color.a)
    }
}