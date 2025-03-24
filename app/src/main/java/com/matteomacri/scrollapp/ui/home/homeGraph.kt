package com.matteomacri.scrollapp.ui.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.matteomacri.scrollapp.ui.home.favorite.Favorite
import com.matteomacri.scrollapp.ui.home.favorite.FavoriteScreen
import com.matteomacri.scrollapp.ui.home.post.Post
import com.matteomacri.scrollapp.ui.home.post.PostScreen

fun NavGraphBuilder.homeGraph(navController: NavController) {
    composable<Post> {
        PostScreen(
            navController = navController
        )
    }
    composable<Favorite> {
        FavoriteScreen(
            navController = navController
        )
    }
}

interface MainTabs