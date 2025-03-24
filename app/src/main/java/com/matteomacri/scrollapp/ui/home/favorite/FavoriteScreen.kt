package com.matteomacri.scrollapp.ui.home.favorite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.matteomacri.scrollapp.ui.core.components.BrandBottomNavigation
import com.matteomacri.scrollapp.ui.home.HomeViewModel
import com.matteomacri.scrollapp.ui.home.MainTabs
import com.matteomacri.scrollapp.ui.post.PostDetail
import kotlinx.serialization.Serializable

@Serializable
data object Favorite : MainTabs

@Composable
fun FavoriteScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val favoritesResource by homeViewModel.favorites.collectAsStateWithLifecycle()

    FavoriteContent(
        favoritesResource = favoritesResource,
        onPostClick = { post ->
            navController.navigate(PostDetail(post.postId))
        },
        bottomBar = { BrandBottomNavigation(navController = navController) }
    )
}