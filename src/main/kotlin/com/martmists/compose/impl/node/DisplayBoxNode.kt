package com.martmists.compose.impl.node

import com.martmists.compose.layout.Alignment
import org.joml.Matrix4f
import kotlin.math.PI

internal class DisplayBoxNode : DisplayNode() {
    var alignment: Alignment = Alignment.Center

    override fun layout() {
        super.layout()
        measure()
    }

    override fun render() {
        val ownSize = measure()
        for (child in children) {
            // TODO: Specific alignment per child somehow?
            val childSize = child.measure()

            val xOffset = when (alignment) {
                Alignment.Start, Alignment.TopStart, Alignment.BottomStart -> (-ownSize.x / 2) + (childSize.x / 2)
                Alignment.Center, Alignment.CenterHorizontally, Alignment.CenterVertically, Alignment.Top, Alignment.Bottom -> 0f
                Alignment.End, Alignment.TopEnd, Alignment.BottomEnd -> (ownSize.x / 2) - (childSize.x / 2)
            }

            val yOffset = when (alignment) {
                Alignment.Top, Alignment.TopStart, Alignment.TopEnd -> ownSize.y - childSize.y
                Alignment.Center, Alignment.CenterHorizontally, Alignment.CenterVertically, Alignment.Start, Alignment.End -> (ownSize.y / 2) - (childSize.y / 2)
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
