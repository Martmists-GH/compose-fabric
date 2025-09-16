package com.martmists.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import com.martmists.compose.DisplayEntityComposable
import com.martmists.compose.DisplayRoot
import com.martmists.compose.impl.DisplayNodeApplier
import com.martmists.compose.impl.node.DisplayTextLiteralNode
import com.martmists.compose.impl.node.DisplayTextObjectNode
import com.martmists.compose.unit.Color
import net.minecraft.text.Style
import net.minecraft.text.Text
import org.joml.Matrix4f

@Composable
@DisplayEntityComposable
fun Text(text: String, style: Style = Style.EMPTY, background: Color = Color(0x40000000), transform: Matrix4f = Matrix4f().identity()) {
    ComposeNode<DisplayTextLiteralNode, DisplayNodeApplier>(
        ::DisplayTextLiteralNode,
    ) {
        set(text) { this.text = it }
        set(style) { this.style = it }
        set(background) { this.background = it.value }
        set(transform) { this.transform = it }
    }
}

@Composable
@DisplayEntityComposable
fun Text(text: Text, background: Color = Color(0x40000000), transform: Matrix4f = Matrix4f().identity()) {
    ComposeNode<DisplayTextObjectNode, DisplayNodeApplier>(
        ::DisplayTextObjectNode,
    ) {
        set(text) { this.text = it }
        set(background) { this.background = it.value }
        set(transform) { this.transform = it }
    }
}
