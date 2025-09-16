package com.martmists.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.martmists.compose.DisplayEntityComposable
import com.martmists.compose.DisplayRoot
import com.martmists.compose.layout.Alignment
import com.martmists.compose.layout.Box
import com.martmists.compose.layout.Column
import com.martmists.compose.unit.Color
import org.joml.Matrix4f
import kotlin.math.abs
import kotlin.math.roundToInt

@Stable
data class SliderStyle(
    val thumbColor: Color = Color.Red,
    val thumbWidth: Float = 0.2f,

    val trackColor: Color = Color.White.copy(alpha = 0x80),
    val trackWidth: Float = 0.2f,  // as fraction of thumbWidth
    val trackHeight: Float = 1f,

    val isDownSlider: Boolean = true,
)

@Composable
@DisplayEntityComposable
fun VerticalSlider(value: Int, range: IntProgression, ticks: Int = 1 + abs(range.last - range.first) / range.step, style: SliderStyle = SliderStyle(), transform: Matrix4f = Matrix4f().identity(), callback: (Int) -> Unit) {
    var chosenIndex by remember {
        var targetIndex = (value - range.first).toFloat() / (range.last - range.first) * (ticks - 1)
        if (!style.isDownSlider) {
            targetIndex = ticks - 1 - targetIndex
        }
        mutableStateOf(targetIndex.roundToInt())
    }

    Box(alignment = Alignment.Bottom, transform = transform) {
        Rect(
            style.trackWidth * style.thumbWidth,
            style.trackHeight,
            style.trackColor,
            Matrix4f().identity().translate(0.01f, 0f, 0f)
        )
        Column {
            repeat(ticks) { i ->
                Clickable(
                    onClick = {
                        chosenIndex = i
                        val delta = (range.last - range.first) / (ticks - 1f)
                        val res = range.first + (if (style.isDownSlider) chosenIndex else ticks - 1 - chosenIndex).toFloat() * delta
                        callback(res.roundToInt())
                    },
                ) {
                    Rect(
                        style.thumbWidth,
                        style.trackHeight/ticks,
                        Color.Transparent,
                    )
                }
            }
        }
        Rect(
            style.thumbWidth,
            style.trackHeight/ticks,
            style.thumbColor,
            transform = Matrix4f().identity()
                .translate(
                    0f,
                    style.trackHeight * ((ticks - 1f - chosenIndex) / ticks),
                    0.001f
                )
        )
    }
}

@Composable
@DisplayEntityComposable
fun VerticalSlider(value: Float, range: ClosedRange<Float>, ticks: Int = 11, style: SliderStyle = SliderStyle(), transform: Matrix4f = Matrix4f().identity(), callback: (Float) -> Unit) {
    var chosenIndex by remember {
        var targetIndex = (value - range.start) / (range.endInclusive - range.start) * (ticks - 1)
        if (!style.isDownSlider) {
            targetIndex = ticks - 1 - targetIndex
        }
        mutableStateOf(targetIndex.roundToInt())
    }

    Box(alignment = Alignment.Bottom, transform = transform) {
        Rect(
            style.trackWidth * style.thumbWidth,
            style.trackHeight,
            style.trackColor,
            Matrix4f().identity().translate(0.01f, 0f, 0f)
        )
        Column {
            repeat(ticks) { i ->
                Clickable(
                    onClick = {
                        chosenIndex = i
                        val delta = (range.endInclusive - range.start) / (ticks - 1)
                        val res = range.start + (if (style.isDownSlider) i else (ticks - 1 - i)) * delta
                        callback(res)
                    },
                ) {
                    Rect(
                        style.thumbWidth,
                        style.trackHeight/ticks,
                        Color.Transparent,
                    )
                }
            }
        }
        Rect(
            style.thumbWidth,
            style.trackHeight/ticks,
            style.thumbColor,
            transform = Matrix4f().identity()
                .translate(
                    0f,
                    style.trackHeight * ((ticks - 1f - chosenIndex) / ticks),
                    0.001f
                )
        )
    }
}
