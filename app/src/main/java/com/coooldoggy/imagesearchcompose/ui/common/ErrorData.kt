package com.coooldoggy.imagesearchcompose.ui.common

import androidx.annotation.IdRes

data class ErrorData(
    val title: String = "",
    @IdRes val image: Int = 0,
    val buttonText: String = ""
)
