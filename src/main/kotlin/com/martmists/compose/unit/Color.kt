package com.martmists.compose.unit

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import org.joml.Vector3f
import org.joml.Vector4f
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Immutable
@JvmInline
value class Color(val value: Int) {
    val a: Int
        get() = value shr 24
    val alpha: Int
        get() = a
    val r: Int
        get() = (value shr 16) and 0xFF
    val red: Int
        get() = r
    val g: Int
        get() = (value shr 8) and 0xFF
    val green: Int
        get() = g
    val b: Int
        get() = value and 0xFF
    val blue: Int
        get() = b

    inline fun copy(alpha: Int = this.alpha, red: Int = this.red, green: Int = this.green, blue: Int = this.blue) = invoke(alpha, red, green, blue)

    fun hsv(): Vector4f {
        if (value and 0xFFFFFF == 0) return Vector4f().zero()

        val r = r / 255f
        val g = g / 255f
        val b = b / 255f
        val max = max(max(r, g), b)
        val min = min(min(r, g), b)
        var delta = max - min
        val s = delta / max

        if (delta == 0f) delta = 1f
        val h = when (max) {
            r -> 60 * (((g - b) / delta) % 6)
            g -> 60 * (((b - r) / delta) + 2)
            else -> 60 * (((r - g) / delta) + 4)
        }
        return Vector4f(a / 255f, h, s, max)
    }

    companion object {
        inline operator fun invoke(a: Int, r: Int, g: Int, b: Int): Color {
            require(a in 0..255)
            require(r in 0..255)
            require(g in 0..255)
            require(b in 0..255)

            val packed = (a shl 24) or
                    (r shl 16) or
                    (g shl 8) or
                    b
            return Color(packed)
        }

        inline fun fromHSV(a: Float, h: Float, s: Float, v: Float): Color {
            require(a in 0f..1f)
            require(h in 0f..360f)
            require(s in 0f..1f)
            require(h in 0f..1f)

            val c = v * s
            val h = h / 60
            val x = c * (1 - abs((h % 2) - 1))
            val m = v - c
            val (r,g, b) = when {
                h < 1 -> Triple(c, x, 0f)
                h < 2 -> Triple(x, c, 0f)
                h < 3 -> Triple(0f, c, x)
                h < 4 -> Triple(0f, x, c)
                h < 5 -> Triple(x, 0f, c)
                else -> Triple(c, 0f, x)
            }
            return invoke(
                (a * 255).roundToInt().coerceAtMost(255),
                ((r + m) * 255).roundToInt().coerceAtMost(255),
                ((g + m) * 255).roundToInt().coerceAtMost(255),
                ((b + m) * 255).roundToInt().coerceAtMost(255),
            )
        }

        @Stable val Black = Color(0xFF000000.toInt())
        @Stable val DarkGray = Color(0xFF444444.toInt())
        @Stable val Gray = Color(0xFF888888.toInt())
        @Stable val LightGray = Color(0xFFCCCCCC.toInt())
        @Stable val White = Color(0xFFFFFFFF.toInt())
        @Stable val Red = Color(0xFFFF0000.toInt())
        @Stable val Green = Color(0xFF00FF00.toInt())
        @Stable val Blue = Color(0xFF0000FF.toInt())
        @Stable val Yellow = Color(0xFFFFFF00.toInt())
        @Stable val Cyan = Color(0xFF00FFFF.toInt())
        @Stable val Magenta = Color(0xFFFF00FF.toInt())
        @Stable val Transparent = Color(0x00000000)
    }
}

