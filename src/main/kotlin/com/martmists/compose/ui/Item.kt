package com.martmists.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.remember
import com.martmists.compose.DisplayEntityComposable
import com.martmists.compose.DisplayRoot
import com.martmists.compose.impl.DisplayNodeApplier
import com.martmists.compose.impl.node.DisplayItemNode
import net.minecraft.item.Item
import net.minecraft.item.ItemDisplayContext
import net.minecraft.item.ItemStack
import org.joml.Matrix4f

@Composable
@DisplayEntityComposable
fun Item(stack: ItemStack, context: ItemDisplayContext = ItemDisplayContext.NONE, transform: Matrix4f = Matrix4f().identity()) {
    ComposeNode<DisplayItemNode, DisplayNodeApplier>(
        ::DisplayItemNode,
    ) {
        set(stack) { this.item = it }
        set(context) { this.itemDisplayContext = it }
        set(transform) { this.transform = it }
    }
}

@Composable
@DisplayEntityComposable
fun Item(item: Item, context: ItemDisplayContext = ItemDisplayContext.NONE, transform: Matrix4f = Matrix4f().identity()) {
    val stack = remember(item) { ItemStack(item) }
    Item(stack, context, transform)
}
