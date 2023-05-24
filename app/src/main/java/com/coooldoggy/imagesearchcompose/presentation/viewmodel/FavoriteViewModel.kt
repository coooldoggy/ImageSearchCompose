package com.coooldoggy.imagesearchcompose.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.coooldoggy.imagesearchcompose.BaseMVIViewModel
import com.coooldoggy.imagesearchcompose.BaseUiEvent
import com.coooldoggy.imagesearchcompose.domain.ResultImage
import com.coooldoggy.imagesearchcompose.domain.toFavoriteEntity
import com.coooldoggy.imagesearchcompose.presentation.FavoriteScreenContract
import com.coooldoggy.imagesearchcompose.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: FavoriteRepository) : BaseMVIViewModel() {
    private val _state = MutableStateFlow(
        FavoriteScreenContract.State(
            favoriteList = emptyList(),
            favoriteChangedState = Pair("", false),
        ),
    )
    val state = _state.asStateFlow()
    val effect = effect<FavoriteScreenContract.Effect>()

    init {
        enableMoreToLoad(false)
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllFavorites().distinctUntilChanged().collect { _favoritesInDB ->
                _state.update {
                    it.copy(
                        favoriteList = _favoritesInDB,
                    )
                }
            }
        }
    }

    private fun addToFavorite(data: ResultImage) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToFavorite(data.toFavoriteEntity())
        }
    }

    private fun removeFromFavorite(data: ResultImage) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavorite(data.url)
        }
    }

    override fun handleEvents(event: BaseUiEvent) {
        when (event) {
            is FavoriteScreenContract.Event.OnClickFavorite -> {
                if (event.data.isFavorite.not()) {
                    addToFavorite(event.data)
                } else {
                    removeFromFavorite(event.data)
                }
                sendEffect {
                    FavoriteScreenContract.Effect.InsertCompleted(event.data)
                }
                _state.update {
                    it.copy(
                        favoriteChangedState = Pair(event.data.url, event.data.isFavorite.not()),
                    )
                }
            }
        }
    }

    override fun loadData() {
    }
}
