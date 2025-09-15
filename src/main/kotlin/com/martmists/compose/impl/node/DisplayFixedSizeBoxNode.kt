package com.martmists.compose.impl.node

import com.martmists.compose.layout.Alignment
import com.martmists.compose.layout.Arrangement
import org.joml.Matrix4f
import org.joml.Vector2f
import kotlin.math.PI

internal class DisplayFixedSizeBoxNode : DisplayNode() {
    var size: Vector2f = Vector2f().zero()
    var alignment: Alignment = Alignment.Center

    override fun measure(): Vector2f {
        children.onEach(DisplayNode::measure)
        return size
    }

    override fun layout() {
        super.layout()
        measure()
    }

    override fun render() {
        for (child in children) {
            // TODO: Specific alignment per child somehow?
            // TODO: What if children are too big?
            val childSize = child.measure()

            val xOffset = when (alignment) {
                Alignment.Start, Alignment.TopStart, Alignment.BottomStart -> (-size.x / 2) + (childSize.x / 2)
                Alignment.Center, Alignment.CenterHorizontally, Alignment.CenterVertically, Alignment.Top, Alignment.Bottom -> 0f
                Alignment.End, Alignment.TopEnd, Alignment.BottomEnd -> (size.x / 2) - (childSize.x / 2)
            }

            val yOffset = when (alignment) {
                Alignment.Top, Alignment.TopStart, Alignment.TopEnd -> size.y - childSize.y
                Alignment.Center, Alignment.CenterHorizontally, Alignment.CenterVertically, Alignment.Start, Alignment.End -> (size.y / 2) - (childSize.y / 2)
                Alignment.Bottom, Alignment.BottomStart, Alignment.BottomEnd -> 0f
            }

            child.worldTransform = Matrix4f().identity()
                .translate(forward.mul(xOffset).rotateY(PI.toFloat() / 2))
                .translate(0f, yOffset, 0f)
                .mul(worldTransform)
                .mul(child.transform)
        }

        children.onEach {
            it.render()
        }
    }
}
