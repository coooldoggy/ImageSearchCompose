package com.coooldoggy.imagesearchcompose.data.network

import com.coooldoggy.imagesearchcompose.Application
import com.coooldoggy.imagesearchcompose.COMMON_PAGE_COUNT
import com.coooldoggy.imagesearchcompose.R
import com.coooldoggy.imagesearchcompose.di.AppModule
import com.coooldoggy.imagesearchcompose.domain.KaKaoImageResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class KakaoApiServiceImpl @Inject constructor(@AppModule.KaKao private val kakaoImageService: KakaoImageService) {
    suspend fun queryKakaoImage(
        query: String,
        startNum: Int = 1,
    ): Flow<Response<KaKaoImageResponse>> =
        flow {
            emit(
                kakaoImageService.queryKaKaoImage(
                    key = Application.getContext()?.getString(R.string.kakao_api_key) ?: "",
                    query = query,
                    page = startNum,
                    size = COMMON_PAGE_COUNT,
                ),
            )
        }
}
