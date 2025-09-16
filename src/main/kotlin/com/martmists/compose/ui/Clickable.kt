package com.martmists.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import com.martmists.compose.DisplayEntityComposable
import com.martmists.compose.DisplayRoot
import com.martmists.compose.impl.DisplayNodeApplier
import com.martmists.compose.impl.node.DisplayClickableNode
import eu.pb4.polymer.virtualentity.api.elements.VirtualElement
import org.joml.Matrix4f

@Composable
@DisplayEntityComposable
fun Clickable(onClick: (isLeftClick: Boolean) -> Unit, transform: Matrix4f = Matrix4f().identity(), contents: @Composable @DisplayEntityComposable () -> Unit) {
    ComposeNode<DisplayClickableNode, DisplayNodeApplier>(
        ::DisplayClickableNode,
        {
            set(onClick) { this.setHandler(onClick) }
            set(transform) { this.transform = it }
        },
        contents,
    )
}

@Composable
@DisplayEntityComposable
fun Clickable(handler: VirtualElement.InteractionHandler, transform: Matrix4f = Matrix4f().identity(), contents: @Composable @DisplayEntityComposable () -> Unit) {
    ComposeNode<DisplayClickableNode, DisplayNodeApplier>(
        ::DisplayClickableNode,
        {
            set(handler) { this.setHandler(handler) }
            set(transform) { this.transform = it }
        },
        contents,
    )
}
