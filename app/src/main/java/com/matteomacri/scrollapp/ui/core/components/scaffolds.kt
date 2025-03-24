package com.matteomacri.scrollapp.ui.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.matteomacri.scrollapp.ui.core.utils.Resource
import com.matteomacri.scrollapp.ui.core.utils.isLoading

@Composable
fun LeanScaffold(
    modifier: Modifier = Modifier,
    resources: List<Resource<*>?> = emptyList(),
    containerColor: Color = MaterialTheme.colorScheme.background,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {

    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        containerColor = containerColor,
        modifier = modifier.fillMaxSize()
    ) { paddingValues -> content(paddingValues) }

    if (resources.isLoading()) {
        BrandLinearLoader(Modifier.fillMaxSize())
    }
}

@Composable
fun BrandLinearLoader(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            )
    ) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
