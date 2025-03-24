package com.matteomacri.scrollapp.ui.home.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matteomacri.scrollapp.R
import com.matteomacri.scrollapp.domain.vo.DbFavoritePost
import com.matteomacri.scrollapp.domain.vo.DbPost
import com.matteomacri.scrollapp.ui.core.components.BrandTopBar
import com.matteomacri.scrollapp.ui.core.components.LeanScaffold
import com.matteomacri.scrollapp.ui.core.utils.LoadingResource
import com.matteomacri.scrollapp.ui.core.utils.Resource
import com.matteomacri.scrollapp.ui.core.utils.SuccessResource
import com.matteomacri.scrollapp.ui.post.component.PostItem
import com.matteomacri.scrollapp.ui.theme.ScrollAppTheme

@Composable
fun FavoriteContent(
    favoritesResource: Resource<List<DbFavoritePost>>,
    onPostClick: (DbFavoritePost) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val favorites = favoritesResource.data ?: emptyList()

    LeanScaffold(
        resources = listOf(favoritesResource),
        topBar = {
            BrandTopBar(
                start = {},
                center = {
                    Text(
                        text = stringResource(R.string.common_favorites),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                end = {}
            )
        },
        bottomBar = bottomBar,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        if (favoritesResource !is LoadingResource) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (favorites.isEmpty()) {
                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                text = stringResource(R.string.no_favorite_post_yet),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                } else {
                    items(favorites, key = { it.postId }) { post ->
                        PostItem(
                            title = post.post.title,
                            description = post.post.body
                        ) {
                            onPostClick(post)
                        }
                    }
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
private fun FavoritePreview() {
    ScrollAppTheme {
        val favorites = SuccessResource(
            listOf(
                DbFavoritePost(
                    postId = 6188,
                    post = DbPost(
                        id = 9640,
                        userId = 7639,
                        title = "mediocritatem",
                        body = "disputationi"
                    )
                )
            )
        )

        FavoriteContent(
            favoritesResource = favorites,
            onPostClick = {},
            bottomBar = {}
        )
    }
}