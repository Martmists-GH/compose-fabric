package com.martmists.compose.impl.node

import eu.pb4.polymer.virtualentity.api.ElementHolder
import java.lang.ref.WeakReference

internal class DisplayRootNode(private val ref: WeakReference<ElementHolder>) : DisplayNode() {
    val holder: ElementHolder
        get() = ref.get()!!
}
