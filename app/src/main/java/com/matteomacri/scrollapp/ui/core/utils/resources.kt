package com.matteomacri.scrollapp.ui.core.utils

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

fun LazyPagingItems<*>.loadStateAsResource(): Resource<Unit>? =
    when (val refreshState = loadState.refresh) {
        LoadState.Loading -> LoadingResource(Unit)
        is LoadState.Error -> ErrorResource(Unit, refreshState.error)
        else -> null
    }


fun List<Resource<*>?>.isLoading(): Boolean = any { it is LoadingResource }

sealed class Resource<out T>(
    open val data: T?
)

data class SuccessResource<out T>(override val data: T?) : Resource<T>(data)
data class LoadingResource<out T>(override val data: T?) : Resource<T>(data)
data class ErrorResource<out T>(override val data: T?, val error: Throwable) : Resource<T>(data)

sealed class Operation

data object SuccessOperation : Operation()
data object LoadingOperation : Operation()
data class ErrorOperation(val error: Throwable) : Operation()

suspend fun tryOperation(fn: suspend () -> Unit) = try {
    fn()
    SuccessOperation
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    ErrorOperation(e)
}

fun <ResultType, RequestType> networkBoundResource(
    query: () -> Flow<ResultType?>,
    fetch: suspend () -> RequestType,
    saveFetchResult: suspend (RequestType) -> Unit,
    onFetchFailed: (Throwable) -> Unit = { },
    shouldFetch: suspend (ResultType?) -> Boolean = { true },
    loadingNull: Boolean = true
) = flow<Resource<ResultType>> {

    val saveFetchResultIO: suspend (RequestType) -> Unit = { request: RequestType ->
        withContext(Dispatchers.IO) { saveFetchResult(request) }
    }

    if (loadingNull) {
        emit(LoadingResource(null))
    }
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(LoadingResource(data))

        try {
            saveFetchResultIO(fetch())
            query().map { SuccessResource(it) }
        } catch (throwable: Throwable) {
            onFetchFailed(throwable)
            query().map { ErrorResource(it, throwable) }
        }
    } else {
        query().map { SuccessResource(it) }
    }

    emitAll(flow)
}