package com.coooldoggy.imagesearchcompose.data.network

import com.coooldoggy.imagesearchcompose.domain.KaKaoImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoImageService {
    @GET("v2/search/image")
    suspend fun queryKaKaoImage(
        @Header("Authorization")key: String,
        @Query("query")query: String,
        @Query("page")page: Int,
        @Query("size")size: Int
    ): Response<KaKaoImageResponse>
}