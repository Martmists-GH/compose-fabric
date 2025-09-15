package com.martmists.compose.impl.node

import eu.pb4.polymer.virtualentity.api.ElementHolder
import eu.pb4.polymer.virtualentity.api.elements.InteractionElement
import eu.pb4.polymer.virtualentity.api.elements.VirtualElement
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector2f
import org.joml.Vector3f
import java.lang.ref.WeakReference
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt

internal class DisplayClickableNode : DisplayNode() {
    private val elements = mutableListOf<InteractionElement>()
    private var handler: VirtualElement.InteractionHandler = VirtualElement.InteractionHandler.EMPTY

    fun setHandler(value: VirtualElement.InteractionHandler) {
        handler = value
        elements.onEach { it.setHandler(value) }
    }

    fun setHandler(callback: (isLeftClick: Boolean) -> Unit) = setHandler(Handler(callback))

    private var lastMeasure: Vector2f = Vector2f().zero()
    override fun measure(): Vector2f {
        return super.measure().also {
            if (it != lastMeasure) {
                correctElementCount(it)
                lastMeasure = it
            }
        }
    }

    private fun correctElementCount(measuredSize: Vector2f) {
        val itemFit = measuredSize.x / ITEM_SIZE
        val requiredCount = (floor(itemFit) + 1).toInt().coerceAtLeast(2)

        holderRef.get()?.also {
            if (requiredCount < elements.size) {
                val toRemove = elements.subList(requiredCount, elements.size)
                toRemove.forEach {
                    holderRef.get()?.removeElement(it)
                }
                elements.removeAll(toRemove)
            } else if (requiredCount > elements.size) {
                val toAdd = List(requiredCount - elements.size) { InteractionElement().apply {
                    height = 0f
                    width = ITEM_SIZE
                    setResponse(true)
                    setHandler(handler)
                } }

                toAdd.forEach {
                    holderRef.get()?.addElement(it)
                }
                elements.addAll(toAdd)
            }
        }
    }

    override fun layout() {
        super.layout()
        measure()
    }

    override fun render() {
        val rot = Matrix4f(worldTransform).getUnnormalizedRotation(Quaternionf()).getEulerAnglesXYZ(Vector3f())
        var yaw = rot.y
        if (rot.x != 0f) {
            // add PI
            yaw = 2 * PI.toFloat() - yaw
        }

        val s2 = sqrt(2f)
        val factor = 1 + abs(sin(yaw * 2f) * s2) * s2 / (1+s2)
        val elementWidth = ITEM_SIZE * factor
        val offsetCenter = worldTransform.getTranslation(Vector3f())
        val containerWidth = lastMeasure.x

        if (elements.size > 1) {
            for ((i, e) in elements.withIndex()) {
                val normalizedPos = (i / (elements.size.toFloat() - 1)) - 0.5f
                val elementOffset = normalizedPos * (containerWidth - elementWidth)
                val translation = Vector3f(offsetCenter).add(Vector3f(elementOffset, 0f, 0f).rotateY(yaw))

                e.height = lastMeasure.y
                e.yaw = yaw
                e.offset = Vec3d(translation.x.toDouble(), offsetCenter.y.toDouble(), translation.z.toDouble())
            }
        } else {
            val e = elements.first()
            val translation = Vector3f(offsetCenter)

            e.height = lastMeasure.y
            e.yaw = yaw
            e.offset = Vec3d(translation.x.toDouble(), offsetCenter.y.toDouble(), translation.z.toDouble())
        }

        children.onEach {
            it.worldTransform = Matrix4f().identity().translate(forward.mul(0.001f)).mul(worldTransform).mul(it.transform)
            it.render()
        }
    }

    private var holderRef: WeakReference<ElementHolder> = WeakReference(null)
    override fun onAdd(holder: ElementHolder) {
        holderRef = WeakReference(holder)
        for (e in elements) {
            holder.addElement(e)
        }
    }

    override fun onRemove(holder: ElementHolder) {
        for (e in elements) {
            holder.removeElement(e)
        }
    }

    private class Handler(private val onClick: (isLeftClick: Boolean) -> Unit) : VirtualElement.InteractionHandler {
        override fun attack(player: ServerPlayerEntity) {
            onClick(true)
        }

        override fun interact(player: ServerPlayerEntity, hand: Hand) {
            onClick(false)
        }

        override fun interactAt(player: ServerPlayerEntity, hand: Hand, pos: Vec3d) {

        }
    }

    companion object {
        const val ITEM_SIZE = 0.125f
    }
}
