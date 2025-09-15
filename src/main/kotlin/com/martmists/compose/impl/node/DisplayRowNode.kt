package com.martmists.compose.impl.node

import com.martmists.compose.layout.Alignment
import com.martmists.compose.layout.Arrangement
import org.joml.Matrix4f
import org.joml.Vector2f
import kotlin.math.PI

internal class DisplayRowNode : DisplayNode() {
    var alignment: Alignment.Vertical = Alignment.Top
    var arrangement: Arrangement = Arrangement.SpacedBy(0f)

    override fun measure(): Vector2f {
        val childMeasures = children.map(DisplayNode::measure)
        return Vector2f(
            childMeasures.map { it.x }.sum(),
            childMeasures.maxOfOrNull { it.y }?.let {
                when (val arr = arrangement) {
                    is Arrangement.SpacedBy -> it + (children.size - 1) * arr.size
                    else -> it
                }
            } ?: 0f,
        )
    }

    override fun render() {
        val ownSize = measure()
        var xOffset = -ownSize.x / 2

        for (child in children) {
            val childSize = child.measure()

            val yOffset = when (alignment) {
                Alignment.Top -> ownSize.y - childSize.y
                Alignment.CenterVertically -> (ownSize.y / 2) - (childSize.y / 2)
                Alignment.Bottom -> 0f
                else -> 0f
            }

            child.worldTransform = Matrix4f().identity()
                .translate(forward.mul(xOffset + childSize.x / 2).rotateY(PI.toFloat() / 2))
                .translate(0f, yOffset, 0f)
                .mul(worldTransform)
                .mul(child.transform)


            xOffset += childSize.x
            when (val arr = arrangement) {
                is Arrangement.SpacedBy -> xOffset += arr.size
            }
        }

        children.onEach {
            it.render()
        }
    }
}
