package com.martmists.compose.impl.node

import eu.pb4.polymer.virtualentity.api.ElementHolder
import eu.pb4.polymer.virtualentity.api.elements.DisplayElement
import eu.pb4.polymer.virtualentity.api.elements.InteractionElement
import eu.pb4.polymer.virtualentity.api.elements.VirtualElement
import net.minecraft.entity.decoration.Brightness
import net.minecraft.entity.decoration.DisplayEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d
import org.joml.Matrix4f
import org.joml.Quaternionfc
import org.joml.Vector3fc
import kotlin.reflect.KProperty

internal abstract class ElementNode : DisplayNode() {
    abstract val element: DisplayElement

    var yaw by element::getYaw to element::setYaw
    var pitch by element::getPitch to element::setPitch


    var translation: Vector3fc by element::getTranslation to element::setTranslation
    var scale: Vector3fc by element::getScale to element::setScale
    var leftRotation: Quaternionfc by element::getLeftRotation to element::setLeftRotation
    var rightRotation: Quaternionfc by element::getRightRotation to element::setRightRotation
    var interpolationDuration by element::getInterpolationDuration to element::setInterpolationDuration
    var teleportDuration by element::getTeleportDuration to element::setTeleportDuration
    var billboardMode: DisplayEntity.BillboardMode by element::getBillboardMode to element::setBillboardMode
    var brightness: Brightness? by element::getBrightness to element::setBrightness
    var viewRange by element::getViewRange to element::setViewRange
    var shadowRadius by element::getShadowRadius to element::setShadowRadius
    var shadowStrength by element::getShadowStrength to element::setShadowStrength
    var displayWidth by element::getDisplayWidth to element::setDisplayWidth
    var displayHeight by element::getDisplayHeight to element::setDisplayHeight
    var glowColorOverride by element::getGlowColorOverride to element::setGlowColorOverride

    override fun render() {
        element.setTransformation(worldTransform)
        element.interpolationDuration = 2
        element.startInterpolationIfDirty()

        super.render()
    }

    override fun onAdd(holder: ElementHolder) {
        holder.addElement(element)
    }

    override fun onRemove(holder: ElementHolder) {
        holder.removeElement(element)
        for (child in children) {
            child.onRemove(holder)
        }
    }

    operator fun <T> Pair<() -> T, (T) -> Unit>.getValue(thisRef: Any?, property: KProperty<*>): T = this.first.invoke()
    operator fun <T> Pair<() -> T, (T) -> Unit>.setValue(thisRef: Any?, property: KProperty<*>, value: T) = this.second.invoke(value)


}
