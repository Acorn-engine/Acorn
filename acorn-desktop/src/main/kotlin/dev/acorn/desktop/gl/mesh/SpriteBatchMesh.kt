package dev.acorn.desktop.gl.mesh

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays

/**
 * Dynamic mesh for batched sprite rendering.
 *
 * Vertex layout per vertex (11 floats):
 * - pos    (vec2)  — pre-transformed world position
 * - uv     (vec2)  — texture coordinates
 * - color  (vec4)  — tint color (RGBA)
 * - local  (vec2)  — original local-space coords for circle mask
 * - mask   (float) — 0 = none, 1 = circle
 *
 * Uses a dynamic VBO re-uploaded each flush and a static IBO
 * pre-computed for [maxSprites] quads.
 */
class SpriteBatchMesh(private val maxSprites: Int) {
    private val vao: Int
    private val vbo: Int
    private val ibo: Int

    companion object {
        const val FLOATS_PER_VERTEX = 11
        const val VERTICES_PER_SPRITE = 4
        const val INDICES_PER_SPRITE = 6
        const val FLOATS_PER_SPRITE = FLOATS_PER_VERTEX * VERTICES_PER_SPRITE
    }

    init {
        vao = glGenVertexArrays()
        glBindVertexArray(vao)

        // Dynamic VBO — allocated to max size, data uploaded per flush
        vbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(
            GL_ARRAY_BUFFER,
            (maxSprites * FLOATS_PER_SPRITE * 4).toLong(),
            GL_DYNAMIC_DRAW
        )

        // Static IBO — pre-computed indices for all possible quads
        val indices = IntArray(maxSprites * INDICES_PER_SPRITE)
        for (i in 0 until maxSprites) {
            val vi = i * 4
            val ii = i * 6
            indices[ii + 0] = vi + 0
            indices[ii + 1] = vi + 1
            indices[ii + 2] = vi + 2
            indices[ii + 3] = vi + 2
            indices[ii + 4] = vi + 3
            indices[ii + 5] = vi + 0
        }
        ibo = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

        // Vertex attributes
        val stride = FLOATS_PER_VERTEX * 4 // bytes
        var offset = 0L

        // location 0: aPos (vec2)
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, offset)
        glEnableVertexAttribArray(0)
        offset += 2 * 4

        // location 1: aUV (vec2)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, offset)
        glEnableVertexAttribArray(1)
        offset += 2 * 4

        // location 2: aColor (vec4)
        glVertexAttribPointer(2, 4, GL_FLOAT, false, stride, offset)
        glEnableVertexAttribArray(2)
        offset += 4 * 4

        // location 3: aLocal (vec2)
        glVertexAttribPointer(3, 2, GL_FLOAT, false, stride, offset)
        glEnableVertexAttribArray(3)
        offset += 2 * 4

        // location 4: aMaskType (float)
        glVertexAttribPointer(4, 1, GL_FLOAT, false, stride, offset)
        glEnableVertexAttribArray(4)

        glBindVertexArray(0)
    }

    /**
     * Uploads vertex data and draws the specified number of sprites.
     */
    fun upload(data: FloatArray, spriteCount: Int) {
        glBindVertexArray(vao)
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferSubData(GL_ARRAY_BUFFER, 0, data)
        glDrawElements(GL_TRIANGLES, spriteCount * INDICES_PER_SPRITE, GL_UNSIGNED_INT, 0L)
        glBindVertexArray(0)
    }
}
