package com.martmists.compose.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.martmists.compose.DisplayRoot
import com.martmists.compose.impl.DisplayNodeApplier
import com.martmists.compose.impl.node.DisplayFixedSizeBoxNode
import org.joml.Matrix4f
import org.joml.Vector2f

@Composable
fun FixedSizeBox(size: Vector2f, alignment: Alignment = Alignment.Center, transform: Matrix4f = Matrix4f().identity(), contents: @Composable () -> Unit) {
    ComposeNode<DisplayFixedSizeBoxNode, DisplayNodeApplier>(
        ::DisplayFixedSizeBoxNode,
        {
            set(transform) { this.transform = it }
            set(alignment) { this.alignment = it }
            set(size) { this.size = it }
        },
        contents,
    )
}

@Composable
fun FixedSizeBox(width: Float, height: Float, alignment: Alignment = Alignment.Center, transform: Matrix4f = Matrix4f().identity(), contents: @Composable () -> Unit) {
    val size = remember(width, height) { Vector2f(width, height) }

    ComposeNode<DisplayFixedSizeBoxNode, DisplayNodeApplier>(
        ::DisplayFixedSizeBoxNode,
        {
            set(transform) { this.transform = it }
            set(alignment) { this.alignment = it }
            set(size) { this.size = it }
        },
        contents,
    )
}
