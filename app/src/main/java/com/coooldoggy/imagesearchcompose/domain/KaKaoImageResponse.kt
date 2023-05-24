package com.coooldoggy.imagesearchcompose.domain

import com.google.gson.annotations.SerializedName

data class KaKaoImageResponse(
    @SerializedName("meta")
    val meta: MetaData,

    @SerializedName("documents")
    val documents: List<Documents>
)

data class MetaData(
    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("pageable_count")
    val pageableCount: Int,

    @SerializedName("is_end")
    val isEnd: Boolean
)

data class Documents(
    @SerializedName("collection")
    val collection: String,

    @SerializedName("thumbnail_url")
    val thumbNailUrl: String,

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("width")
    val width: Int,

    @SerializedName("height")
    val height: Int,

    @SerializedName("display_sitename")
    val displaySiteName: String,

    @SerializedName("doc_url")
    val docUrl: String,

    @SerializedName("datetime")
    val dateTime: String
)