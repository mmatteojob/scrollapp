package com.matteomacri.scrollapp.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matteomacri.scrollapp.domain.vo.DbPost
import com.matteomacri.scrollapp.repo.FavoriteRepository
import com.matteomacri.scrollapp.repo.PostRepository
import com.matteomacri.scrollapp.ui.core.GenericDataMap
import com.matteomacri.scrollapp.ui.core.utils.LoadingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    val post = GenericDataMap { postId: Int ->
        postRepository.getPost(postId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                LoadingResource(null)
            )
    }

    val isFavorite = GenericDataMap { postId: Int ->
        favoriteRepository.isFavorite(postId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                LoadingResource(false)
            )
    }

    suspend fun addPostToFavorite(post: DbPost) =
        favoriteRepository.addPostToFavorites(post)

    suspend fun deletePostFromFavorite(postId: Int) =
        favoriteRepository.deletePostFromFavorites(postId)
}