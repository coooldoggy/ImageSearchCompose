package com.coooldoggy.imagesearchcompose

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.coooldoggy.imagesearchcompose.domain.ImageSource
import com.coooldoggy.imagesearchcompose.domain.ResultImage
import com.coooldoggy.imagesearchcompose.presentation.FavoriteScreenContract
import com.coooldoggy.imagesearchcompose.presentation.SearchScreenContract
import com.coooldoggy.imagesearchcompose.presentation.viewmodel.FavoriteViewModel
import com.coooldoggy.imagesearchcompose.presentation.viewmodel.SearchViewModel
import com.coooldoggy.imagesearchcompose.ui.common.BasicTextField
import com.coooldoggy.imagesearchcompose.ui.common.NoItemData
import com.coooldoggy.imagesearchcompose.ui.common.SystemItem
import com.coooldoggy.imagesearchcompose.util.composableActivityViewModel
import com.coooldoggy.imagesearchcompose.util.onClick
import com.coooldoggy.weverseimagesearch.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel) {
    val state = viewModel.state.collectAsState()
    val commonState = viewModel.commonState.collectAsState()
    val listState = rememberLazyStaggeredGridState()
    val favoriteViewModel = composableActivityViewModel<FavoriteViewModel>()
    Effect(favoriteViewModel)

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(onDoneClickListener = { _query ->
            viewModel.sendEvent(SearchScreenContract.Event.OnClickSearch(_query))
        }, onDeleteClickListener = {
            viewModel.sendEvent(SearchScreenContract.Event.OnDeleteClick)
        }, query = state.value.searchQuery)

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            state = listState,
        ) {
            state.value.searchResult.forEach {
                item {
                    ResultImageItem(data = it, onClickFavoriteListener = { _resultImage ->
                        favoriteViewModel.sendEvent(FavoriteScreenContract.Event.OnClickFavorite(_resultImage))
                    })
                }
            }
            if (state.value.searchQuery.isNotEmpty()) {
                item {
                    SystemItem(
                        modifier = Modifier.fillMaxWidth(),
                        noItemData = NoItemData(""),
                        listState = listState,
                        uiLoadingState = commonState.value,
                        viewModel = viewModel,
                    )
                }
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
                    val text = if (_effect.data.isFavorite.not()) "좋아요에 추가되었습니다." else "좋아요에서 삭제되었습니다."
                    Toast.makeText(Application.getContext(), text, Toast.LENGTH_SHORT).show()
                }
            }
        }.collect()
    }
}

@Composable
fun ResultImageItem(data: ResultImage, onClickFavoriteListener: (ResultImage) -> Unit) {
    val favoriteViewModel = composableActivityViewModel<FavoriteViewModel>()
    val favoriteState = favoriteViewModel.state.collectAsState()
    val isFavoriteStatus = remember {
        mutableStateOf(data.isFavorite)
    }

    LaunchedEffect(favoriteState.value.favoriteChangedState) {
        val itemUrl = favoriteState.value.favoriteChangedState.first
        val isFavorite = favoriteState.value.favoriteChangedState.second

        if (data.url == itemUrl) {
            isFavoriteStatus.value = isFavorite
            data.isFavorite = isFavorite
        }
    }

    Box {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data.url)
                .error(R.drawable.ic_warning)
                .crossfade(true)
                .build(),
            contentDescription = "",
            modifier = Modifier
                .padding(10.dp),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = if (data.imageSource == ImageSource.NAVER) "Naver" else "Kakao",
            modifier = Modifier.align(
                Alignment.BottomStart,
            ).background(Color.Black).padding(10.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )
        Image(
            painter = if (isFavoriteStatus.value) painterResource(id = R.drawable.baseline_favorite_24) else painterResource(id = R.drawable.baseline_favorite_border_24),
            contentDescription = "좋아요",
            modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp).onClick {
                onClickFavoriteListener.invoke(data)
            },
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(query: String, onDoneClickListener: (String) -> Unit, onDeleteClickListener: () -> Unit) {
    var isVisibleCancel by remember {
        mutableStateOf(false)
    }

    var inputText by remember {
        mutableStateOf(query)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = inputText) {
        isVisibleCancel = inputText.isNotEmpty()
    }

    BasicTextField(
        value = inputText,
        valueChangeListener = { _newText ->
            inputText = _newText
        },
        keyboardActions = KeyboardActions(
            onDone = {
                coroutineScope.launch {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
                onDoneClickListener.invoke(inputText)
            },
        ),
        leftContent = {
            Image(
                painter = painterResource(id = R.drawable.baseline_search_24),
                contentDescription = "검색",
                modifier = Modifier.padding(10.dp),
            )
        },
        rightContent = {
            AnimatedVisibility(
                visible = isVisibleCancel,
                enter = slideInVertically(),
                exit = slideOutVertically(),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_cancel_24),
                    contentDescription = "취소",
                    modifier = Modifier
                        .padding(10.dp)
                        .onClick {
                            inputText = ""
                            onDeleteClickListener.invoke()
                        },
                )
            }
        },
    )
}
