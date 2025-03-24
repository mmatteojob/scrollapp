package com.matteomacri.scrollapp.ui.home.post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.matteomacri.scrollapp.R
import com.matteomacri.scrollapp.domain.vo.DbPost
import com.matteomacri.scrollapp.ui.core.components.BrandTopBar
import com.matteomacri.scrollapp.ui.core.components.LeanScaffold
import com.matteomacri.scrollapp.ui.core.utils.loadStateAsResource
import com.matteomacri.scrollapp.ui.core.utils.toPrettyDescription
import com.matteomacri.scrollapp.ui.post.component.PostItem
import com.matteomacri.scrollapp.ui.theme.ScrollAppTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun PostContent(
    postsPagingItems: LazyPagingItems<DbPost>,
    searchQuery: String,
    isLoading: Boolean,
    onPostClick: (DbPost) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LeanScaffold(
        resources = listOf(postsPagingItems.loadStateAsResource()),
        topBar = {
            BrandTopBar(
                start = {},
                center = {
                    Text(
                        text = stringResource(R.string.common_posts),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                end = {},
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = bottomBar,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        text = stringResource(R.string.common_search)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )

            LazyColumn(
                state = rememberLazyListState(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier
                    .weight(1F)
            ) {

                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    items(
                        count = postsPagingItems.itemCount,
                        key = postsPagingItems.itemKey { it.id }
                    ) { index ->
                        val post = postsPagingItems[index]

                        if (post != null) {
                            PostItem(
                                title = post.title,
                                description = post.body.toPrettyDescription(),
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                onPostClick(post)
                            }
                        }
                    }

                }

                item {
                    if (postsPagingItems.loadState.append is LoadState.Loading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PostPreview() {
    ScrollAppTheme {
        PostContent(
            postsPagingItems = flowOf(
                PagingData.from(
                    listOf(
                        DbPost(
                            1,
                            2,
                            "title",
                            "body"
                        )
                    )
                )
            ).collectAsLazyPagingItems(),
            searchQuery = "",
            isLoading = false,
            onSearchQueryChange = {},
            onPostClick = {},
            bottomBar = {}
        )
    }
}