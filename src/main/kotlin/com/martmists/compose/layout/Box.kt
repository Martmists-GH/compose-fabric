package com.martmists.compose.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.martmists.compose.DisplayRoot
import com.martmists.compose.impl.DisplayNodeApplier
import com.martmists.compose.impl.node.DisplayBoxNode
import com.martmists.compose.impl.node.DisplayFixedSizeBoxNode
import org.joml.Matrix4f
import org.joml.Vector2f

@Composable
fun Box(alignment: Alignment = Alignment.Center, transform: Matrix4f = Matrix4f().identity(), contents: @Composable () -> Unit) {
    ComposeNode<DisplayBoxNode, DisplayNodeApplier>(
        ::DisplayBoxNode,
        {
            set(transform) { this.transform = it }
            set(alignment) { this.alignment = it }
        },
        contents,
    )
}
