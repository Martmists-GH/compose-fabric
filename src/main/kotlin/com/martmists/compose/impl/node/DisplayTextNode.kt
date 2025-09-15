package com.martmists.compose.impl.node

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.martmists.compose.impl.util.FontProvider
import com.mojang.serialization.JsonOps
import eu.pb4.polymer.virtualentity.api.elements.TextDisplayElement
import net.minecraft.resource.ResourceManager
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import java.io.InputStreamReader
import kotlin.math.PI

internal abstract class DisplayTextNode : ElementNode() {
    override val element = TextDisplayElement()

    var background: Int by element::getBackground to element::setBackground
    var lineWidth: Int by element::getLineWidth to element::setLineWidth
    var textOpacity: Byte by element::getTextOpacity to element::setTextOpacity
    var shadow: Boolean by element::getShadow to element::setShadow

    private fun measureText(): Vector2f {
        val resMgr = if (resourceManagerAvailable) {
            resourceManager
        } else {
            element.holder?.attachment?.world?.server?.resourceManager.also {
                if (it != null) {
                    resourceManager = it
                    resourceManagerAvailable = true
                }
            }
        }

        if (resMgr != null) {
            if (!fontProviderAvailable) {
                val (id, res) = resMgr.findResources("font") { it.path.endsWith(".glyphs.json") }.toList().first()
                val reader = InputStreamReader(res.inputStream)
                val providers = FontProvider.LIST_CODEC.parse(
                    JsonOps.INSTANCE,
                    Gson().fromJson(reader, JsonElement::class.java)
                ).resultOrPartial(::error).orElseThrow()
                if (providers != null) {
                    fontProvider = providers
                    fontProviderAvailable = true
                }
            }

            val width = getWidthFromProviders(element.text.string)
            return Vector2f(width.toFloat(), 10f)
        } else {
            val defaultHeight = 10f
            val defaultWidth = 3f * element.text.string.length

            return Vector2f(defaultWidth, defaultHeight)
        }
    }

    override fun measure(): Vector2f {
        val size = measureText().mul(1/40f)
        val scale = worldTransform.getScale(Vector3f())
        return Vector2f(size.x * scale.x, size.y * scale.y)
    }

    override fun render() {
        element.setTransformation(Matrix4f().identity().translate(forward.mul(-0.1f/9).rotateY(PI.toFloat() / 2)).mul(worldTransform))
        element.interpolationDuration = 2
        element.startInterpolationIfDirty()
    }

    companion object {
        var resourceManagerAvailable = false
        lateinit var resourceManager: ResourceManager
        var fontProviderAvailable = false
        lateinit var fontProvider: List<FontProvider>

        fun getWidthFromProviders(text: String): Int {
            var width = 0

            for (c in text) {
                val cs = c.toString()
                for (fontProvider in fontProvider) {
                    if (!fontProvider.advances.isEmpty() && fontProvider.advances.containsKey(cs)) {
                        width += fontProvider.advances[cs]!!
                        break
                    } else if (!fontProvider.chars.isEmpty() && fontProvider.containsChar(c)) {
                        width += fontProvider.ascent
                        break
                    } else if (fontProvider.glyphSizes != null) {
                        width += fontProvider.getGlyphWidth(c.code)
                        break
                    }
                }
            }

            if (text.isNotEmpty()) {
                width += 1
            }

            return width
        }
    }
}
