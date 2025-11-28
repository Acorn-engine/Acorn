package dev.acorn.core


@JvmInline
value class Color(val argb: Int) {
    companion object {
        fun fromARGB(argb: Int): Color = Color(argb)

        fun fromRGB(rgb: Int): Color =
            Color((0xFF shl 24) or (rgb and 0x00FFFFFF))

        val WHITE = fromRGB(0xFFFFFF)
        val BLACK = fromRGB(0x000000)
        val RED   = fromRGB(0xFF0000)
        val GREEN = fromRGB(0x00FF00)
        val BLUE  = fromRGB(0x0000FF)
    }

    val a: Float get() = ((argb ushr 24) and 0xFF) / 255f
    val r: Float get() = ((argb ushr 16) and 0xFF) / 255f
    val g: Float get() = ((argb ushr 8) and 0xFF) / 255f
    val b: Float get() = (argb and 0xFF) / 255f
}