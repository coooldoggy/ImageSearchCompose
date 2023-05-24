package com.coooldoggy.imagesearchcompose.presentation

import com.coooldoggy.imagesearchcompose.BaseUiEffect
import com.coooldoggy.imagesearchcompose.BaseUiEvent
import com.coooldoggy.imagesearchcompose.BaseUiState
import com.coooldoggy.imagesearchcompose.data.database.FavoriteEntity
import com.coooldoggy.imagesearchcompose.domain.ResultImage

class FavoriteScreenContract {
    data class State(
        val favoriteList: List<FavoriteEntity>,
        val favoriteChangedState: Pair<String, Boolean>
    ) : BaseUiState
    sealed class Event : BaseUiEvent {
        data class OnClickFavorite(val data: ResultImage): Event()
    }
    sealed class Effect : BaseUiEffect {
        data class InsertCompleted(val data: ResultImage): Effect()
    }
}
