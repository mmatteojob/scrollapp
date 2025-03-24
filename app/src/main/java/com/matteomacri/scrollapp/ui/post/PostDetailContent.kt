package com.matteomacri.scrollapp.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matteomacri.scrollapp.R
import com.matteomacri.scrollapp.domain.vo.DbPost
import com.matteomacri.scrollapp.ui.core.components.BrandTopBar
import com.matteomacri.scrollapp.ui.core.components.LeanScaffold
import com.matteomacri.scrollapp.ui.core.utils.LoadingResource
import com.matteomacri.scrollapp.ui.core.utils.Resource
import com.matteomacri.scrollapp.ui.core.utils.SuccessResource
import com.matteomacri.scrollapp.ui.theme.ScrollAppTheme

@Composable
fun PostDetailContent(
    resources: List<Resource<*>?>,
    postResource: Resource<DbPost>?,
    isFavoriteResource: Resource<Boolean>,
    onBack: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val post = postResource?.data
    val isFavorite = isFavoriteResource.data

    LeanScaffold(
        resources = resources,
        topBar = {
            BrandTopBar(
                start = {
                    IconButton(
                        onClick = { onBack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                },
                center = {
                    Text(
                        text = if (postResource is LoadingResource) "" else
                            stringResource(R.string.post_no, post?.id.toString()),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                end = {
                    if (isFavoriteResource !is LoadingResource && postResource !is LoadingResource) {
                        IconButton(
                            onClick = { onFavoriteClick() }
                        ) {
                            Icon(
                                imageVector = if (isFavorite == true) Icons.Default.Favorite
                                else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {},
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { paddingValues ->
        if (postResource !is LoadingResource) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = post?.title.orEmpty(),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outline)
                )
                Text(
                    text = post?.body.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(top = 24.dp)
                )
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
private fun PostDetailPreview() {
    ScrollAppTheme {
        val post = DbPost(
            id = 6197,
            userId = 3843,
            title = "malorum",
            body = "aenean"
        )
        PostDetailContent(
            resources = emptyList(),
            postResource = SuccessResource(post),
            isFavoriteResource = SuccessResource(false),
            onFavoriteClick = {},
            onBack = {}
        )
    }
}