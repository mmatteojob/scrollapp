package com.matteomacri.scrollapp.ui.post

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.matteomacri.scrollapp.ui.core.rememberResult
import com.matteomacri.scrollapp.ui.core.utils.LoadingResource
import kotlinx.serialization.Serializable

@Serializable
data class PostDetail(
    val postId: Int
)

@Composable
fun PostDetailScreen(
    args: PostDetail,
    navController: NavController,
    postViewModel: PostViewModel = hiltViewModel()
) {
    val postResource by postViewModel.post[args.postId]
        .collectAsStateWithLifecycle(LoadingResource(null))

    val isFavorite by postViewModel.isFavorite[args.postId]
        .collectAsStateWithLifecycle(LoadingResource(false))

    val (setFavoriteResult, setFavorite) = rememberResult { ->
        if (isFavorite.data == true) {
            postViewModel.deletePostFromFavorite(args.postId)
        } else {
            postResource.data?.let {
                postViewModel.addPostToFavorite(it)
            }
        }
    }

    PostDetailContent(
        resources = listOf(setFavoriteResult),
        postResource = postResource,
        isFavoriteResource = isFavorite,
        onFavoriteClick = { setFavorite() },
        onBack = { navController.navigateUp() }
    )
}