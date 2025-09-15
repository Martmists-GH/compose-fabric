package com.martmists.compose.impl.node

import net.minecraft.text.Text

internal class DisplayTextObjectNode : DisplayTextNode() {
    var text: Text by element::text
}
