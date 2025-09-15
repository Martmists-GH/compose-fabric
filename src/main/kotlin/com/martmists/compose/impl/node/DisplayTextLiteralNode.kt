package com.martmists.compose.impl.node

import net.minecraft.text.Style
import net.minecraft.text.Text

internal class DisplayTextLiteralNode : DisplayTextNode() {
    var text: String = ""
        set(value) {
            field = value
            element.text = Text.literal(value).fillStyle(style)
        }
    var style: Style = Style.EMPTY
        set(value) {
            field = value
            element.text = Text.literal(text).fillStyle(value)
        }
}
