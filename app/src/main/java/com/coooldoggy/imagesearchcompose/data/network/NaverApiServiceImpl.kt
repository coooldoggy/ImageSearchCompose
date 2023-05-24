package com.coooldoggy.imagesearchcompose.data.network

import com.coooldoggy.imagesearchcompose.Application
import com.coooldoggy.imagesearchcompose.COMMON_PAGE_COUNT
import com.coooldoggy.imagesearchcompose.R
import com.coooldoggy.imagesearchcompose.di.AppModule
import com.coooldoggy.imagesearchcompose.domain.NaverImageResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class NaverApiServiceImpl @Inject constructor(@AppModule.Naver private val naverImageService: NaverImageService) {
    suspend fun queryNaverImage(
        query: String,
        startNum: Int,
    ): Flow<Response<NaverImageResponse>> =
        flow {
            emit(
                naverImageService.queryNaverImage(
                    query = query,
                    start = startNum,
                    clientId = Application.getContext()?.getString(
                        R.string.naver_client_id,
                    ) ?: "",
                    clientSecret = Application.getContext()?.getString(R.string.naver_client_secret) ?: "",
                    display = COMMON_PAGE_COUNT,
                ),
            )
        }
}
