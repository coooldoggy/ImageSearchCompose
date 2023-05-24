package com.coooldoggy.imagesearchcompose.data.network

import com.coooldoggy.imagesearchcompose.domain.NaverImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverImageService {
    @GET("search/image")
    suspend fun queryNaverImage(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") query: String,
        @Query("display") display: Int,
        @Query("start") start: Int
    ): Response<NaverImageResponse>
}