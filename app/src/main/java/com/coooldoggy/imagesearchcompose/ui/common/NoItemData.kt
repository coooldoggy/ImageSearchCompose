package com.coooldoggy.imagesearchcompose.ui.common

import androidx.annotation.IdRes

data class NoItemData(
    val title: String = "데이터가 없습니다.",
    @IdRes val image: Int = 0,
    val buttonText: String = "",
    val changeHeight: Int = 0,
    val marginTop: Int = 0
)
