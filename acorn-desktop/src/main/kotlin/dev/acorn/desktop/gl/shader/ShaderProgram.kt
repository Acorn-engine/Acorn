package dev.acorn.desktop.gl.shader

import org.lwjgl.opengl.GL20.*

/**
 * An OpenGL shader program wrapper, it creates and compiles a vertex + fragment shader, links them into a single program, and provides methods for binding and setting uniforms
 *
 * @param vertexSrc GLSL source code for the vertex shader
 * @param fragmentSrc GLSL source code for the fragment shader
 * @throws IllegalStateException if compilation or linking fails
 */
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

    /**
     * Binds this program (glUseProgram)
     */
    fun use() = glUseProgram(id)

    /**
     * Sets an integer uniform by name
     *
     * @param name uniform name in GLSL (e.g. "uTex")
     * @param v integer value
     *
     */
    fun uniform1i(name: String, v: Int) =
        glUniform1i(glGetUniformLocation(id, name), v)

    /**
     * Sets a float uniform by name
     *
     * @param name uniform name in GLSL (e.g. "uTex")
     * @param v float value
     */
    fun uniform1f(name: String, v: Float) =
        glUniform1f(glGetUniformLocation(id, name), v)

    /**
     * Sets a vec4 uniform by name (4 floats)
     * Most used for colors/tints
     *
     * @param name uniform name in GLSL (e.g. "uTex")
     */
    fun uniform4f(name: String, a: Float, b: Float, c: Float, d: Float) =
        glUniform4f(glGetUniformLocation(id, name), a, b, c, d)

    /**
     * Deletes the underlying OpenGL program object
     * After calling this, [id] is no longer valid and the program must not be used
     */
    fun delete() = glDeleteProgram(id)

    /**
     * Compiles a shader of the given type from GLSL source code
     *
     * @param type GL_VERTEX_SHADER or GL_FRAGMENT_SHADER
     * @param src GLSL shader source
     * @return OpenGL shader object ID
     * @throws IllegalStateException if compilation fails
     */
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