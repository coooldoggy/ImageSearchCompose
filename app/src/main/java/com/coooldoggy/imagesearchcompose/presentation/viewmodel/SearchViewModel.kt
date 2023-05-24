package com.coooldoggy.imagesearchcompose.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.coooldoggy.imagesearchcompose.BaseMVIViewModel
import com.coooldoggy.imagesearchcompose.BaseUiEvent
import com.coooldoggy.imagesearchcompose.COMMON_PAGE_COUNT
import com.coooldoggy.imagesearchcompose.domain.ResultImage
import com.coooldoggy.imagesearchcompose.domain.toCommonImage
import com.coooldoggy.imagesearchcompose.presentation.SearchScreenContract
import com.coooldoggy.imagesearchcompose.repository.FavoriteRepository
import com.coooldoggy.imagesearchcompose.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: FavoriteRepository,
    private val networkRepository: NetworkRepository,
) : BaseMVIViewModel() {
    private val _state = MutableStateFlow(
        SearchScreenContract.State(
            searchQuery = "",
            searchResult = mutableListOf(),
        ),
    )
    val state = _state.asStateFlow()

    private var naverTotalCount = 0
    private val naverPageCount = AtomicInteger(1)
    private val NAVER_MAX_COUNT = 1000

    private val kakaoPageCount = AtomicInteger(1)

    override fun loadData() {
        if (state.value.searchQuery.isNotEmpty()) {
            queryImage(state.value.searchQuery)
        }
    }

    override fun handleEvents(event: BaseUiEvent) {
        when (event) {
            is SearchScreenContract.Event.OnClickSearch -> {
                _state.update {
                    it.copy(
                        searchQuery = event.query,
                    )
                }
            }
            is SearchScreenContract.Event.OnDeleteClick -> {
                _state.update {
                    it.copy(
                        searchQuery = "",
                        searchResult = emptyList(),
                    )
                }
                resetCount()
            }
            is SearchScreenContract.Event.OnClickFavorite -> {
                val result: List<ResultImage> = state.value.searchResult.filter {
                    it.url == event.data.url
                }.map {
                    ResultImage(
                        imageSource = it.imageSource,
                        url = it.url,
                        isFavorite = event.data.isFavorite,
                    )
                }
                _state.update { state ->
                    state.copy(
                        searchResult = result,
                    )
                }
            }
        }
    }

    private fun resetCount() {
        naverTotalCount = 0
        naverPageCount.getAndSet(1)
        kakaoPageCount.getAndSet(1)
    }

    private fun queryImage(query: String) {
        cancelActiveFlowJob()
        viewModelScope.launch(activeFlowJob) {
            val naverFlow =
                networkRepository.queryNaverImage(query = query, startNum = naverPageCount.get())
            val kakaoFlow =
                networkRepository.queryKakaoImage(query = query, startNum = kakaoPageCount.get())
            naverFlow.combineTransform(kakaoFlow) { _naverResult, _kakaoResult ->
                var naverResult: List<ResultImage> = listOf()
                var kakaoResult: List<ResultImage> = listOf()
                var isEndKakao = false
                if (_naverResult.isSuccessful) {
                    naverTotalCount = _naverResult.body()?.total ?: 0
                    naverResult =
                        _naverResult.body()?.items?.map { it.toCommonImage(repository.isRowExist(it.link)) }
                            ?: emptyList()

                    if (naverPageCount.get() <= naverTotalCount.div(COMMON_PAGE_COUNT) && naverPageCount.get() != NAVER_MAX_COUNT) {
                        naverPageCount.incrementAndGet()
                    }
                } else {
                    setErrorState(_naverResult.errorBody().toString())
                }
                if (_kakaoResult.isSuccessful) {
                    isEndKakao = _kakaoResult.body()?.meta?.isEnd ?: false
                    kakaoResult = _kakaoResult.body()?.documents?.map {
                        it.toCommonImage(
                            repository.isRowExist(it.imageUrl),
                        )
                    } ?: emptyList()
                    kakaoPageCount.incrementAndGet()
                } else {
                    setErrorState(_kakaoResult.errorBody().toString())
                }

                emit(naverResult + kakaoResult)
                enableMoreToLoad(
                    isEndKakao.not() && naverPageCount.get() < naverTotalCount.div(
                        COMMON_PAGE_COUNT,
                    ) && naverPageCount.get() != NAVER_MAX_COUNT,
                )
            }.collect {
                addData(it as? List<ResultImage> ?: emptyList())
            }
        }
    }

    private fun addData(data: List<ResultImage>) {
        _state.getAndUpdate {
            if (it.searchResult.isEmpty()) {
                it.copy(
                    searchResult = data,
                )
            } else {
                it.copy(
                    searchResult = it.searchResult + data,
                )
            }
        }
    }
}
