package com.martmists.compose.layout

import androidx.compose.runtime.Composable
import com.martmists.compose.DisplayEntityComposable
import com.martmists.compose.DisplayRoot
import org.joml.Matrix4f

@Composable
@DisplayEntityComposable
fun Collapsible(expanded: Boolean, transform: Matrix4f = Matrix4f().identity(), contents: @Composable @DisplayEntityComposable () -> Unit) {
    val scaledHeight = if (expanded) 1f else 0.0001f

    Box(transform = Matrix4f().scale(1f, scaledHeight, 1f).mul(transform), contents = contents)
}
