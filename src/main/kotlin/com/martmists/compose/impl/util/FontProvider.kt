package com.martmists.compose.impl.util

import com.martmists.compose.impl.node.DisplayTextNode
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.Identifier
import org.apache.commons.lang3.StringUtils
import java.io.IOException

// Converted to Kotlin from:
// https://github.com/Awakened-Redstone/neoskies/blob/dev/src/main/java/com/awakenedredstone/neoskies/font/FontProvider.java
// Permission from the author has been granted to release this class under BSD-3-Clause.
internal data class FontProvider(
    val type: String,
    val advances: MutableMap<String, Int>,
    val sizes: String,
    val height: Int,
    val ascent: Int,
    val chars: MutableList<String>
) {
    val glyphSizes: ByteArray?

    init {
        var fontSizes: ByteArray? = null

        if (StringUtils.isNotBlank(sizes)) {
            try {
                DisplayTextNode.resourceManager.open(Identifier.tryParse(sizes)).use { inputStream ->
                    fontSizes = inputStream.readNBytes(65536)
                }
            } catch (e: IOException) {
                // TODO: Log
            }
        }
        glyphSizes = fontSizes
    }

    fun getGlyphWidth(codePoint: Int): Int {
        if (glyphSizes == null) {
            return 0
        }
        if (codePoint < 0 || codePoint >= glyphSizes.size) {
            return 0
        }
        return glyphSizes[codePoint].toInt()
    }

    fun containsChar(c: Char): Boolean {
        for (string in chars) {
            if (string.contains(c.toString())) return true
        }
        return false
    }

    companion object {
        val CODEC: Codec<FontProvider> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.fieldOf("type").forGetter(FontProvider::type),
                Codec.unboundedMap<String, Int>(Codec.STRING, Codec.INT).optionalFieldOf("advances", emptyMap()).forGetter(FontProvider::advances),
                Codec.STRING.optionalFieldOf("sizes", "").forGetter(FontProvider::sizes),
                Codec.INT.optionalFieldOf("height", 8).forGetter(FontProvider::height),
                Codec.INT.optionalFieldOf("ascent", 0).forGetter<FontProvider>(FontProvider::ascent),
                Codec.list<String>(Codec.STRING).optionalFieldOf("chars", emptyList()).forGetter(FontProvider::chars)
            ).apply(instance) { type, advances, sizes, height, ascent, chars ->
                FontProvider(
                    type,
                    advances,
                    sizes,
                    height,
                    ascent,
                    chars
                )
            }
        }

        val LIST_CODEC: Codec<MutableList<FontProvider>> = CODEC.listOf().fieldOf("providers").codec()
    }
}
