package com.coooldoggy.imagesearchcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.coooldoggy.imagesearchcompose.presentation.viewmodel.FavoriteViewModel
import com.coooldoggy.imagesearchcompose.presentation.viewmodel.SearchViewModel
import com.coooldoggy.imagesearchcompose.ui.theme.WeverseImageSearchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val favoriteViewModel by viewModels<FavoriteViewModel>()
    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeverseImageSearchTheme {
                ImageSearchApp(favoriteViewModel = favoriteViewModel, searchViewModel = searchViewModel)
            }
        }
    }
}
