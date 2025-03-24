package com.matteomacri.scrollapp.ui.core

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

@Composable
fun ProviderList(
    vararg providerList: @Composable (content: @Composable () -> Unit) -> Unit,
    content: @Composable () -> Unit
) = providerList.reversed().fold(content) { acc, provider ->
    { provider(acc) }
}()

object EmptyViewModelStoreOwner : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore
        get() = ViewModelStore()
}


val LocalActivityViewModelStoreOwner: ProvidableCompositionLocal<ViewModelStoreOwner>
    @Composable get() = staticCompositionLocalOf {
        EmptyViewModelStoreOwner
    }

@Composable
fun ActivityViewModelProvider(
    activity: ComponentActivity,
    content: @Composable () -> Unit,
) {
    val activityViewModelStoreOwner = rememberActivityViewModelStoreOwner(activity)

    CompositionLocalProvider(LocalActivityViewModelStoreOwner provides activityViewModelStoreOwner) {
        content()
    }
}

@Composable
private fun rememberActivityViewModelStoreOwner(activity: ComponentActivity): ViewModelStoreOwner {
    val context = LocalContext.current
    return remember(context) { activity }
}