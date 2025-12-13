package dev.acorn.desktop.gl.shader

import org.lwjgl.opengl.GL20.*

class ShaderProgram(vertexSrc: String, fragmentSrc: String) {
    val id: Int

    init {
        val vs = compile(GL_VERTEX_SHADER, vertexSrc)
        val fs = compile(GL_FRAGMENT_SHADER, fragmentSrc)

        id = glCreateProgram()
        glAttachShader(id, vs)
        glAttachShader(id, fs)
        glLinkProgram(id)

        if(glGetProgrami(id, GL_LINK_STATUS) == 0) {
            val log = glGetProgramInfoLog(id)
            throw IllegalStateException("Shader link failed: \n$log")
        }

        glDeleteShader(vs)
        glDeleteShader(fs)
    }

    fun use() = glUseProgram(id)

    fun uniform1i(name: String, v: Int) = glUniform1i(glGetUniformLocation(id, name), v)
    fun uniform1f(name: String, v: Float) = glUniform1f(glGetUniformLocation(id, name), v)
    fun uniform4f(name: String, a: Float, b: Float, c: Float, d: Float) =
        glUniform4f(glGetUniformLocation(id, name), a, b, c, d)

    fun delete() = glDeleteProgram(id)
    private fun compile(type: Int, src: String): Int {
        val s = glCreateShader(type)
        glShaderSource(s, src)
        glCompileShader(s)
        if(glGetShaderi(s, GL_COMPILE_STATUS) == 0) {
            val log = glGetShaderInfoLog(s)
            throw IllegalStateException("Shader compile failed:\n$log")
        }

        return s
    }
}