package com.coooldoggy.imagesearchcompose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.coooldoggy.imagesearchcompose.presentation.FavoriteScreen
import com.coooldoggy.imagesearchcompose.presentation.viewmodel.FavoriteViewModel
import com.coooldoggy.imagesearchcompose.presentation.viewmodel.SearchViewModel
import com.coooldoggy.imagesearchcompose.ui.common.BottomNavItem

@Composable
fun ImageSearchApp(favoriteViewModel: FavoriteViewModel, searchViewModel: SearchViewModel) {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            BottomNavigation(navController = navController)
        },
    ) { _innerPadding ->
        Box(modifier = Modifier.padding(_innerPadding)) {
            BottomNavigationGraph(navController = navController, favoriteViewModel = favoriteViewModel, searchViewModel = searchViewModel)
        }
    }
}

@Composable
fun BottomNavigationGraph(navController: NavHostController, favoriteViewModel: FavoriteViewModel, searchViewModel: SearchViewModel) {
    NavHost(navController = navController, startDestination = BottomNavItem.Search.screenRoute) {
        composable(BottomNavItem.Search.screenRoute) {
            SearchScreen(searchViewModel)
        }
        composable(BottomNavItem.Favorite.screenRoute) {
            FavoriteScreen(favoriteViewModel)
        }
    }
}

@SuppressLint("ResourceType")
@Composable
fun BottomNavigation(navController: NavHostController) {
    val navItems = listOf(BottomNavItem.Search, BottomNavItem.Favorite)
    androidx.compose.material.BottomNavigation(
        backgroundColor = Color.Blue,
        contentColor = Color.White,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        navItems.forEach { _item ->
            BottomNavigationItem(
                selected = currentRoute == _item.screenRoute,
                onClick = {
                    navController.navigate(_item.screenRoute) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = _item.icon),
                        contentDescription = stringResource(
                            id = _item.title,
                        ),
                        modifier = Modifier.size(26.dp),
                    )
                },
                label = { Text(stringResource(id = _item.title), fontSize = 9.sp) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color(0xFFB7BBBF),
                alwaysShowLabel = false,
            )
        }
    }
}
