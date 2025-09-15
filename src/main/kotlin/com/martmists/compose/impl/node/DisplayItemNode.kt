package com.martmists.compose.impl.node

import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement
import net.minecraft.item.ItemDisplayContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f

internal class DisplayItemNode : ElementNode() {
    override val element = ItemDisplayElement()
    var item: ItemStack by element::getItem to element::setItem
    var itemDisplayContext: ItemDisplayContext by element::getItemDisplayContext to element::setItemDisplayContext
    private val displayContext: DisplayContext
        get() = DisplayContext.entries.first { it.mc == itemDisplayContext }

    init {
        item = ItemStack(Items.DIAMOND_PICKAXE)
        itemDisplayContext = ItemDisplayContext.FIXED
    }

    override fun measure(): Vector2f {
        val scale = worldTransform.getScale(Vector3f())
        val w = maxOf(scale.x, scale.y) * 15 / 16
        val h = scale.y * 15 / 16
        return Vector2f(w, h)
    }

    override fun render() {
        element.setTransformation(worldTransform
            .mul(Matrix4f(transform).invert())
            .translate(displayContext.dx, displayContext.dy, displayContext.dz)
            .mul(transform))
        element.interpolationDuration = 2
        element.startInterpolationIfDirty()
    }

    enum class DisplayContext(val mc: ItemDisplayContext, val dx: Float, val dy: Float, val dz: Float, val dw: Float, val dh: Float) {
        NONE(ItemDisplayContext.NONE, 0f, 0.5f, 0f, 1f, 1f),
        THIRD_PERSON_LEFT_HAND(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, 0f, 0.3f, 0f, 1f, 1f),
        THIRD_PERSON_RIGHT_HAND(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, 0f, 0.3f, 0f, 1f, 1f),
        FIRST_PERSON_LEFT_HAND(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, 0f, 0.2f, 0f, 1f, 1f),
        FIRST_PERSON_RIGHT_HAND(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, 0f, 0.2f, 0f, 1f, 1f),
        HEAD(ItemDisplayContext.HEAD, 0f, 0.5f, 0f, 1f, 1f),
        GUI(ItemDisplayContext.GUI, 0f, 0.5f, 0f, 1f, 1f),
        GROUND(ItemDisplayContext.GROUND, 0f, 0.1f, 0f, 1f, 1f),
        FIXED(ItemDisplayContext.FIXED, 0f, 0.5f, 0f, 1f, 1f),
    }
}
