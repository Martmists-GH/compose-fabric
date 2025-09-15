package com.martmists.compose.impl

import androidx.compose.runtime.AbstractApplier
import com.martmists.compose.impl.node.DisplayNode
import com.martmists.compose.impl.node.DisplayRootNode

internal class DisplayNodeApplier(root: DisplayRootNode) : AbstractApplier<DisplayNode>(root) {
    private val holder = root.holder

    override fun insertTopDown(index: Int, instance: DisplayNode) {
        current.children.add(index, instance)
        instance.onAdd(holder)
    }

    override fun insertBottomUp(index: Int, instance: DisplayNode) {
        // N/A
    }

    override fun remove(index: Int, count: Int) {
        val toRemove = current.children.subList(index, index + count)
        for (c in toRemove) {
            c.onRemove(holder)
        }
        current.children.remove(index, count)
    }

    override fun move(from: Int, to: Int, count: Int) {
        current.children.move(from, to, count)
    }

    override fun onClear() {
        root.children.clear()
    }
}
