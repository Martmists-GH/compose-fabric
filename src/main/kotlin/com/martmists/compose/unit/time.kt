package com.martmists.compose.unit

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

val Int.ticks: Duration
    get() = (this * 50).milliseconds
