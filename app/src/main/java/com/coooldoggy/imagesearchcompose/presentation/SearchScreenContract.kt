package com.coooldoggy.imagesearchcompose.presentation

import com.coooldoggy.imagesearchcompose.BaseUiEffect
import com.coooldoggy.imagesearchcompose.BaseUiEvent
import com.coooldoggy.imagesearchcompose.BaseUiState
import com.coooldoggy.imagesearchcompose.domain.ResultImage

class SearchScreenContract {
    data class State(
        val searchQuery: String,
        val searchResult: List<ResultImage>,
    ) : BaseUiState
    sealed class Event : BaseUiEvent {
        data class OnClickSearch(val query: String) : Event()
        object OnDeleteClick : Event()
        data class OnClickFavorite(val data: ResultImage) : Event()
    }
    sealed class Effect : BaseUiEffect
}
