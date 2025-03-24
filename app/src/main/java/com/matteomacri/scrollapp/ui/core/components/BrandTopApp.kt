package com.matteomacri.scrollapp.ui.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandTopBar(
    modifier: Modifier = Modifier,
    start: (@Composable () -> Unit)?,
    center: (@Composable () -> Unit)?,
    end: (@Composable () -> Unit)?
) {
    CenterAlignedTopAppBar(
        modifier = modifier
            .fillMaxWidth(),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = Color.Black,
        ),
        title = {
            center?.invoke()
        },
        navigationIcon = {
            start?.invoke()
        },
        actions = {
            end?.invoke()
        }
    )
}

@Preview
@Composable
private fun BrandTopBarPreview() {
    BrandTopBar(
        start = {},
        center = {
            Text(
                text = "Top app bar",
                color = Color.White
            )
        },
        end = {}
    )
}