package dev.acorn.core.math

import kotlin.math.max
import kotlin.math.min

fun clamp(v: Float, lo: Float, hi: Float): Float = max(lo, min(hi, v))