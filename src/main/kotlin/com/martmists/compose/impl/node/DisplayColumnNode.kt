package com.martmists.compose.impl.node

import com.martmists.compose.layout.Alignment
import com.martmists.compose.layout.Arrangement
import org.joml.Matrix4f
import org.joml.Vector2f
import kotlin.math.PI

internal class DisplayColumnNode : DisplayNode() {
    var alignment: Alignment.Horizontal = Alignment.Start
    var arrangement: Arrangement = Arrangement.SpacedBy(0f)

    override fun measure(): Vector2f {
        val childMeasures = children.map(DisplayNode::measure)
        return Vector2f(
            childMeasures.maxOfOrNull { it.x }?.let {
                when (val arr = arrangement) {
                    is Arrangement.SpacedBy -> it + (children.size - 1) * arr.size
                    else -> it
                }
            } ?: 0f,
            childMeasures.map { it.y }.sum(),
        )
    }

    override fun layout() {
        super.layout()
        measure()
    }

    override fun render() {
        val ownSize = measure()
        var yOffset = ownSize.y

        for (child in children) {
            val childSize = child.measure()

            val xOffset = when (alignment) {
                Alignment.Start -> (-ownSize.x / 2) + (childSize.x / 2)
                Alignment.CenterHorizontally -> 0f
                Alignment.End -> (ownSize.x / 2) - (childSize.x / 2)
                else -> 0f
            }

            child.worldTransform = Matrix4f().identity()
                .translate(forward.mul(xOffset).rotateY(PI.toFloat() / 2))
                .translate(0f, yOffset - childSize.y, 0f)
                .mul(worldTransform)
                .mul(child.transform)


            yOffset -= childSize.y
            when (val arr = arrangement) {
                is Arrangement.SpacedBy -> yOffset -= arr.size
            }
        }

        children.onEach {
            it.render()
        }
    }
}
