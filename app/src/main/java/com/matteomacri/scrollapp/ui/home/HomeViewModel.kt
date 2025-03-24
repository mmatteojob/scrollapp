package com.matteomacri.scrollapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.matteomacri.scrollapp.repo.FavoriteRepository
import com.matteomacri.scrollapp.repo.PostRepository
import com.matteomacri.scrollapp.ui.core.utils.LoadingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val posts = postRepository.getPosts().cachedIn(viewModelScope)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val filteredPosts = combine(
        posts, _searchQuery
    ) { pagingData, query ->
        if (query.isNotEmpty()) {
            pagingData.filter { it.title.contains(query, ignoreCase = true) }
        } else {
            pagingData
        }
    }.cachedIn(viewModelScope)


    val favorites = favoriteRepository.getFavorites()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            LoadingResource(null)
        )

    fun updateSearchQuery(query: String) = _searchQuery.update { query }
}