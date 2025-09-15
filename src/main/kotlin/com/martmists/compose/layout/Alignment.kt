package com.martmists.compose.layout

import androidx.compose.runtime.Stable

@Stable
sealed interface Alignment {
    sealed interface Vertical : Alignment
    sealed interface Horizontal : Alignment

    object Top : Vertical
    object CenterVertically : Vertical
    object Bottom : Vertical

    object Start : Horizontal
    object CenterHorizontally : Horizontal
    object End : Horizontal

    object Center : Alignment
    object TopStart : Alignment
    object TopEnd : Alignment
    object BottomStart : Alignment
    object BottomEnd : Alignment
}
