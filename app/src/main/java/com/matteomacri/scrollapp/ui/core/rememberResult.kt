package com.matteomacri.scrollapp.ui.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.matteomacri.scrollapp.ui.core.utils.ErrorResource
import com.matteomacri.scrollapp.ui.core.utils.LoadingResource
import com.matteomacri.scrollapp.ui.core.utils.Resource
import com.matteomacri.scrollapp.ui.core.utils.SuccessResource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun <T, U> rememberResult(
    fn: suspend (U) -> T
): Pair<Resource<T>?, (U) -> Unit> {
    var result by remember { mutableStateOf<Resource<T>?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val resultFn = { u: U ->
        coroutineScope.launch {
            try {
                result = LoadingResource(null)
                result = SuccessResource(fn(u))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                result = ErrorResource(null, e)
            }
        }
        Unit
    }

    return result to resultFn
}

@Composable
fun <T> rememberResult(
    fn: suspend () -> T
): Pair<Resource<T>?, () -> Unit> {
    val (result, resultFn) = rememberResult<T, Unit> { fn() }
    return result to { resultFn(Unit) }
}