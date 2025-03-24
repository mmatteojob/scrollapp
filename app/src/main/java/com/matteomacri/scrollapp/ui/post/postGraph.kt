package com.matteomacri.scrollapp.ui.post

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute

fun NavGraphBuilder.postGraph(navController: NavController) {
    composable<PostDetail> { backStackEntry ->
        val args = backStackEntry.toRoute<PostDetail>()
        PostDetailScreen(
            args = args,
            navController = navController
        )
    }
}