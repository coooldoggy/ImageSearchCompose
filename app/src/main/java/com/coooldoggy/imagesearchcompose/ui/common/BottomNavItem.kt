package com.coooldoggy.imagesearchcompose.ui.common

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import com.coooldoggy.imagesearchcompose.FAVORITE_VIEW
import com.coooldoggy.imagesearchcompose.SEARCH_VIEW
import com.coooldoggy.weverseimagesearch.R

sealed class BottomNavItem(
    @IdRes val title: Int,
    @IdRes val icon: Int,
    val screenRoute: String,
) {
    @SuppressLint("ResourceType")
    object Search : BottomNavItem(title = R.string.search_screen, icon = R.drawable.baseline_search_24_white, screenRoute = SEARCH_VIEW)

    @SuppressLint("ResourceType")
    object Favorite : BottomNavItem(title = R.string.favorite_screen, icon = R.drawable.baseline_favorite_24, screenRoute = FAVORITE_VIEW)
}
