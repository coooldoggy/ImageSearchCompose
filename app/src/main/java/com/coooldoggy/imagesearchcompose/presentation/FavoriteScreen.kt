package com.coooldoggy.imagesearchcompose.presentation

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.coooldoggy.imagesearchcompose.Application
import com.coooldoggy.imagesearchcompose.data.database.FavoriteEntity
import com.coooldoggy.imagesearchcompose.domain.toResultImage
import com.coooldoggy.imagesearchcompose.presentation.viewmodel.FavoriteViewModel
import com.coooldoggy.imagesearchcompose.util.onClick
import com.coooldoggy.weverseimagesearch.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteScreen(viewModel: FavoriteViewModel) {
    val state = viewModel.state.collectAsState()
    val listState = rememberLazyStaggeredGridState()
    Effect(viewModel)

    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2), modifier = Modifier.fillMaxWidth(), state = listState) {
        state.value.favoriteList.forEach {
            item {
                FavoriteItem(data = it, onClickDeleteFavoriteListener = { _data ->
                    viewModel.sendEvent(FavoriteScreenContract.Event.OnClickFavorite(_data.toResultImage()))
                })
            }
        }
    }
}

@Composable
private fun Effect(viewModel: FavoriteViewModel) {
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.onEach { _effect ->
            when (_effect) {
                is FavoriteScreenContract.Effect.InsertCompleted -> {
                    Toast.makeText(Application.getContext(), "좋아요에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }.collect()
    }
}

@Composable
fun FavoriteItem(data: FavoriteEntity, onClickDeleteFavoriteListener: (FavoriteEntity) -> Unit) {
    Box {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data.imageUrl)
                .error(R.drawable.ic_warning)
                .crossfade(true)
                .build(),
            contentDescription = "",
            modifier = Modifier
                .padding(10.dp),
            contentScale = ContentScale.Crop,
        )
        Image(
            painter = painterResource(id = R.drawable.baseline_favorite_24),
            contentDescription = "좋아요",
            modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp).onClick {
                onClickDeleteFavoriteListener.invoke(data)
            },
        )
    }
}
