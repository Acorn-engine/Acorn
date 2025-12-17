package dev.acorn.desktop.window

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.GL_TRUE

/**
 * Applies the default GLFW window and OpenGL context hints used by Acorn
 */
object GlfwHints {
    /**
     * Applies GLFW window hints prior to window creation
     * Must be called before glfwCreateWindow()
     */
    fun apply() {
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
    }
}