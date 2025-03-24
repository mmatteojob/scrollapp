package com.matteomacri.scrollapp.ui.core.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.matteomacri.scrollapp.R
import com.matteomacri.scrollapp.ui.home.MainTabs
import com.matteomacri.scrollapp.ui.home.favorite.Favorite
import com.matteomacri.scrollapp.ui.home.post.Post


data class BottomBarItem(
    val route: MainTabs,
    val icon: ImageVector,
    @StringRes val label: Int
)

private val Home = BottomBarItem(
    route = Post,
    icon = Icons.Default.Home,
    label = R.string.common_posts
)

private val Favorites = BottomBarItem(
    route = Favorite,
    icon = Icons.Default.Favorite,
    label = R.string.common_favorites
)


val NavigationBarDestinations = listOf(
    Home,
    Favorites
)

@Composable
fun BrandBottomNavigation(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val previousRoute = navController.previousBackStackEntry?.destination?.route
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    fun navigateTo(navigationBarModel: BottomBarItem) =
        navController.navigate(route = navigationBarModel.route) {
            launchSingleTop = true
            restoreState = true
            popUpTo(Post) {
                saveState = true
            }
        }

    NavigationBar(
        modifier = modifier,
        tonalElevation = 10.dp,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary
    ) {

        val tabRoute = navBackStackEntry.fromRoute(
            previousRoute = previousRoute,
            tabOptions = NavigationBarDestinations.map { it.route },
            otherOptions = listOf()
        )

        NavigationBarDestinations.forEach { screen ->
            val selected = tabRoute == screen.route
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondary,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    selectedIconColor = MaterialTheme.colorScheme.background
                ),
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = stringResource(screen.label),
                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                alwaysShowLabel = false,
                selected = selected,
                onClick = { navigateTo(screen) }
            )
        }
    }
}

inline fun <reified T : Any> NavBackStackEntry?.fromRoute(
    previousRoute: String?,
    tabOptions: List<T>,
    otherOptions: List<T>
): T? {
    val parentRoute = previousRoute?.trimSimplifiedRoute()

    this?.destination?.route?.trimSimplifiedRoute().let { route ->
        val matchedOption = tabOptions.find { it::class.simpleName == route }
        if (matchedOption != null) {
            return matchedOption
        }

        val matchedOtherOption = otherOptions.find { it::class.simpleName == route }
        if (matchedOtherOption != null) {
            return tabOptions.find { it::class.simpleName == parentRoute }
        }

        return null
    }
}

fun String.trimSimplifiedRoute(): String {
    return this.substringBefore("?")
        .substringBefore("/")
        .substringAfterLast(".")
}