package com.martmists.compose.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import com.martmists.compose.DisplayRoot
import com.martmists.compose.impl.DisplayNodeApplier
import com.martmists.compose.impl.node.DisplayRowNode
import org.joml.Matrix4f

@Composable
fun Row(alignment: Alignment.Vertical = Alignment.Top, arrangement: Arrangement = Arrangement.SpacedBy(0f), transform: Matrix4f = Matrix4f().identity(), contents: @Composable () -> Unit) {
    ComposeNode<DisplayRowNode, DisplayNodeApplier>(
        ::DisplayRowNode,
        {
            set(transform) { this.transform = it }
            set(alignment) { this.alignment = it }
            set(arrangement) { this.arrangement = it }
        },
        contents,
    )
}
