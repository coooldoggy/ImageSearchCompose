package com.coooldoggy.imagesearchcompose.domain

import com.coooldoggy.imagesearchcompose.data.database.FavoriteEntity

fun ResultImage.toFavoriteEntity(): FavoriteEntity {
    return FavoriteEntity(
        imageUrl = url,
        source = imageSource.name
    )
}

fun FavoriteEntity.toResultImage(): ResultImage {
    return ResultImage(
        imageSource = if (source == "Naver") ImageSource.NAVER else ImageSource.KAKAO,
        url = imageUrl,
        isFavorite = true
    )
}