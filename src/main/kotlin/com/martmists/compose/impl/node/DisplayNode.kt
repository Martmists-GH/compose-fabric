package com.martmists.compose.impl.node

import eu.pb4.polymer.virtualentity.api.ElementHolder
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector2f
import org.joml.Vector3f

internal abstract class DisplayNode {
    val children = mutableListOf<DisplayNode>()
    var transform = Matrix4f().identity()
    var worldTransform = Matrix4f().identity()
    val forward: Vector3f
        get() = Vector3f(0f, 0f, 1f).rotate(worldTransform.getUnnormalizedRotation(Quaternionf())).normalize()

    open fun measure(): Vector2f {
        val scale = worldTransform.getScale(Vector3f())
        val ownSize = children.map(DisplayNode::measure).max()
        return Vector2f(ownSize.x * scale.x, ownSize.y * scale.y)
    }

    protected fun List<Vector2f>.max(): Vector2f = Vector2f(
        maxOfOrNull { it.x } ?: 0f,
        maxOfOrNull { it.y } ?: 0f,
    )

    open fun onAdd(holder: ElementHolder) {}
    open fun onRemove(holder: ElementHolder) {}

    open fun layout() {
        children.forEach {
            it.layout()
        }
    }
    
    open fun render() {
        children.onEach {
            it.worldTransform = Matrix4f(it.transform).mul(worldTransform)
            it.render()
        }
    }
}
