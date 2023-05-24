package com.coooldoggy.imagesearchcompose.domain

import com.google.gson.annotations.SerializedName

data class NaverImageResponse(
    @SerializedName("lastBuildDate")
    val lastBuildDate: String,
    @SerializedName("total")
    val total: Int,
    @SerializedName("start")
    val start: Int,
    @SerializedName("display")
    val display: Int,
    @SerializedName("items")
    val items: List<NaverImageItem>
)

data class NaverImageItem(
    @SerializedName("title")
    val title: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("thumbnail")
    val thumbNail: String,
    @SerializedName("sizeheight")
    val sizeHeight: Int,
    @SerializedName("sizewidth")
    val sizeWidth: Int,
)
