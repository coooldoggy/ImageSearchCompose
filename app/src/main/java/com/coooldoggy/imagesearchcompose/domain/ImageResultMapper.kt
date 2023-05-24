package com.coooldoggy.imagesearchcompose.domain

fun Documents.toCommonImage(isFavorite: Boolean): ResultImage {
    return ResultImage(
        imageSource = ImageSource.KAKAO,
        url = imageUrl,
        isFavorite = isFavorite
    )
}

fun NaverImageItem.toCommonImage(isFavorite: Boolean): ResultImage {
    return ResultImage(
        imageSource = ImageSource.NAVER,
        url = link,
        isFavorite = isFavorite
    )
}

