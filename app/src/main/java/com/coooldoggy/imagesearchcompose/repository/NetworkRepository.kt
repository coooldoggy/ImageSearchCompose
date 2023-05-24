package com.coooldoggy.imagesearchcompose.repository

import com.coooldoggy.imagesearchcompose.data.network.KakaoApiServiceImpl
import com.coooldoggy.imagesearchcompose.data.network.NaverApiServiceImpl
import com.coooldoggy.imagesearchcompose.domain.KaKaoImageResponse
import com.coooldoggy.imagesearchcompose.domain.NaverImageResponse
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

@ViewModelScoped
class NetworkRepository @Inject constructor(
    private val naverApiServiceImpl: NaverApiServiceImpl,
    private val kakaoApiServiceImpl: KakaoApiServiceImpl,
) {
    suspend fun queryNaverImage(
        query: String,
        startNum: Int,
    ): Flow<Response<NaverImageResponse>> = naverApiServiceImpl.queryNaverImage(query, startNum)

    suspend fun queryKakaoImage(
        query: String,
        startNum: Int = 1,
    ): Flow<Response<KaKaoImageResponse>> = kakaoApiServiceImpl.queryKakaoImage(query, startNum)
}
