package com.martmists.compose.ui

import androidx.compose.runtime.Composable
import com.martmists.compose.DisplayRoot
import com.martmists.compose.unit.Color
import org.joml.Matrix4f

@Composable
fun Rect(width: Float, height: Float, color: Color = Color(0x40000000), transform: Matrix4f = Matrix4f().identity()) {
    Text(
        text = "  ",
        background = color,
        transform = Matrix4f(transform).scale(40*width / 9, 40*height / 10, 1f)
    )
}
