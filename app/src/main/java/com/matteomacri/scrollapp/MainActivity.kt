package com.matteomacri.scrollapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.matteomacri.scrollapp.ui.core.ActivityViewModelProvider
import com.matteomacri.scrollapp.ui.core.ProviderList
import com.matteomacri.scrollapp.ui.home.homeGraph
import com.matteomacri.scrollapp.ui.home.post.Post
import com.matteomacri.scrollapp.ui.post.postGraph
import com.matteomacri.scrollapp.ui.theme.ScrollAppTheme
import com.matteomacri.scrollapp.ui.theme.tweenAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            ProviderList(
                { ActivityViewModelProvider(activity = this, content = it) },
                { ScrollAppTheme(content = it) },
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Post,
                    enterTransition = { fadeIn(animationSpec = tweenAnimation) },
                    exitTransition = { fadeOut(animationSpec = tweenAnimation) }
                ) {
                    homeGraph(navController)
                    postGraph(navController)
                }
            }
        }
    }
}