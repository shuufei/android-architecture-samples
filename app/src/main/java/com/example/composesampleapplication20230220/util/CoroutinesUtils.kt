package com.example.composesampleapplication20230220.util

import kotlinx.coroutines.flow.SharingStarted

private const val StopTimeoutMillis: Long = 5000
val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)
