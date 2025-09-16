package com.martmists.compose.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import com.martmists.compose.DisplayEntityComposable
import com.martmists.compose.DisplayRoot
import com.martmists.compose.impl.DisplayNodeApplier
import com.martmists.compose.impl.node.DisplayColumnNode
import org.joml.Matrix4f

@Composable
@DisplayEntityComposable
fun Column(alignment: Alignment.Horizontal = Alignment.Start, arrangement: Arrangement = Arrangement.SpacedBy(0f), transform: Matrix4f = Matrix4f().identity(), contents: @Composable @DisplayEntityComposable () -> Unit) {
    ComposeNode<DisplayColumnNode, DisplayNodeApplier>(
        ::DisplayColumnNode,
        {
            set(transform) { this.transform = it }
            set(alignment) { this.alignment = it }
            set(arrangement) { this.arrangement = it }
        },
        contents,
    )
}
