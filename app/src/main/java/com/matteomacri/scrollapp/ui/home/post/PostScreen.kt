package com.matteomacri.scrollapp.ui.home.post

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.matteomacri.scrollapp.ui.core.components.BrandBottomNavigation
import com.matteomacri.scrollapp.ui.core.utils.LoadingResource
import com.matteomacri.scrollapp.ui.core.utils.loadStateAsResource
import com.matteomacri.scrollapp.ui.home.HomeViewModel
import com.matteomacri.scrollapp.ui.home.MainTabs
import com.matteomacri.scrollapp.ui.post.PostDetail
import kotlinx.serialization.Serializable

@Serializable
data object Post : MainTabs

@Composable
fun PostScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val postsPagingItems = homeViewModel.filteredPosts.collectAsLazyPagingItems()
    val searchQuery by homeViewModel.searchQuery.collectAsStateWithLifecycle()
    val isLoading = postsPagingItems.loadState.refresh == LoadState.Loading ||
            postsPagingItems.loadStateAsResource() is LoadingResource

    PostContent(
        postsPagingItems = postsPagingItems,
        searchQuery = searchQuery,
        isLoading = isLoading,
        onPostClick = { post ->
            navController.navigate(PostDetail(post.id))
        },
        onSearchQueryChange = {
            homeViewModel.updateSearchQuery(it)
        },
        bottomBar = { BrandBottomNavigation(navController = navController) }
    )
}