package com.coooldoggy.imagesearchcompose.domain


enum class ImageSource{
    NAVER,
    KAKAO
}

data class ResultImage(
    val imageSource: ImageSource,
    val url: String,
    var isFavorite: Boolean
)
