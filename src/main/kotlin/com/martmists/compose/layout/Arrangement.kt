package com.martmists.compose.layout

import androidx.compose.runtime.Stable

@Stable
sealed interface Arrangement {
    class SpacedBy(val size: Float) : Arrangement
}
