package dev.acorn.desktop.window

import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.stb.STBImage.*
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.Platform
import java.nio.ByteBuffer

/**
 * Small wrapper around a GLFW window
 * This class creates the window and makes its OpenGL context current, provides commonly used operations (poll, swap, close check), and exposes window size (logical units) and framebuffer size (pixel units)
 *
 * HiDPI note:
 * - On Retina/HiDPI displays, framebuffer size can be larger than window size. Use framebuffer size for glViewport, and window size for projection/game coordinates
 */
class GlfwWindow(width: Int, height: Int, title: String, fullscreen: Boolean) {
    val handle: Long

    init {
        GlfwHints.apply()

        val monitor = if(fullscreen) glfwGetPrimaryMonitor() else NULL
        handle = glfwCreateWindow(width, height, title, monitor, NULL)
        require(handle != NULL) { "Failed to create GLFW window" }

        glfwMakeContextCurrent(handle)
        glfwSwapInterval(1)
        glfwShowWindow(handle)
    }

    /** Whether icon setting is supported on this platform (Linux/Windows only) */
    val supportsIcon: Boolean = Platform.get() != Platform.MACOSX

    /** @return true if the user requested the window to close */
    fun shouldClose(): Boolean = glfwWindowShouldClose(handle)
    /** Processes pending OS/window events (input, resize, close, etc.) */
    fun pollEvents() = glfwPollEvents()
    /** Swaps front/back buffers to present the rendered frame */
    fun swapBuffers() = glfwSwapBuffers(handle)

    /**
     * Writes the framebuffer size into [out] as [width, height]
     * This value should be used for glViewport
     */
    fun framebufferSize(out: IntArray) {
        val w = IntArray(1)
        val h = IntArray(1)
        glfwGetFramebufferSize(handle, w, h)
        out[0] = w[0]
        out[1] = h[0]
    }

    /**
     * Writes the window size into [out] as [width, height]
     * This value should be used for projection/game coordinates
     */
    fun windowSize(out: IntArray) {
        val w = IntArray(1)
        val h = IntArray(1)
        glfwGetWindowSize(handle, w, h)

        out[0] = w[0]
        out[1] = h[0]
    }

    /** Destroys the GLFW window, should be called during shutdown */
    fun destroy() = glfwDestroyWindow(handle)

    /**
     * Sets the window icon from a resource path.
     * Creates multiple icon sizes (16x16, 32x32, 48x48) for best OS compatibility.
     * Uses nearest-neighbor scaling to preserve pixel art quality.
     *
     * @param resourcePath path to the icon resource (e.g., "myicon.png"), or null to clear the icon
     */
    fun setIcon(resourcePath: String?) {
        if (!supportsIcon) return

        if (resourcePath == null) {
            // Clear the icon (revert to OS default)
            glfwSetWindowIcon(handle, null)
            return
        }

        val resource = javaClass.classLoader.getResource(resourcePath) ?: return

        val bytes = resource.openStream().use { it.readBytes() }
        val buffer = BufferUtils.createByteBuffer(bytes.size)
        buffer.put(bytes)
        buffer.flip()

        stackPush().use { stack ->
            val w = stack.mallocInt(1)
            val h = stack.mallocInt(1)
            val channels = stack.mallocInt(1)

            stbi_set_flip_vertically_on_load(false)

            val originalImage = stbi_load_from_memory(buffer, w, h, channels, 4) ?: return
            val originalWidth = w.get()
            val originalHeight = h.get()

            try {
                val sizes = listOf(16, 32, 48)
                val images = GLFWImage.malloc(sizes.size, stack)

                sizes.forEachIndexed { index, size ->
                    val scaledPixels = if (size == originalWidth && size == originalHeight) {
                        // Use original image directly
                        val copy = BufferUtils.createByteBuffer(size * size * 4)
                        copy.put(originalImage)
                        originalImage.rewind()
                        copy.flip()
                        copy
                    } else {
                        // Scale using nearest-neighbor to preserve pixel art quality
                        scaleNearestNeighbor(originalImage, originalWidth, originalHeight, size)
                    }

                    images.position(index)
                    images.width(size)
                    images.height(size)
                    images.pixels(scaledPixels)
                }

                images.position(0)
                glfwSetWindowIcon(handle, images)
            } finally {
                stbi_image_free(originalImage)
            }
        }
    }

    /**
     * Scales an RGBA image using nearest-neighbor interpolation.
     * This preserves sharp edges for pixel art without introducing blur.
     */
    private fun scaleNearestNeighbor(
        source: ByteBuffer,
        srcWidth: Int,
        srcHeight: Int,
        destSize: Int
    ): ByteBuffer {
        val dest = BufferUtils.createByteBuffer(destSize * destSize * 4)

        for (destY in 0 until destSize) {
            for (destX in 0 until destSize) {
                val srcX = (destX * srcWidth) / destSize
                val srcY = (destY * srcHeight) / destSize
                val srcIndex = (srcY * srcWidth + srcX) * 4
                val destIndex = (destY * destSize + destX) * 4

                dest.put(destIndex, source.get(srcIndex))         // R
                dest.put(destIndex + 1, source.get(srcIndex + 1)) // G
                dest.put(destIndex + 2, source.get(srcIndex + 2)) // B
                dest.put(destIndex + 3, source.get(srcIndex + 3)) // A
            }
        }

        return dest
    }
}