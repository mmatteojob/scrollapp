package com.matteomacri.scrollapp.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator

@OptIn(ExperimentalPagingApi::class)
class GenericRemoteMediator<RequestType, ResultType : Any>(
    private val getLocalItemCount: suspend () -> Int?,
    private val fetch: suspend (Int) -> Pair<RequestType, Int>,
    private val saveFetchResult: suspend (RequestType, clearLocalItems: Boolean) -> Unit
) : RemoteMediator<Int, ResultType>() {

    @ExperimentalPagingApi
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ResultType>
    ): MediatorResult {

        return try {
            val nextPage = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> getLocalItemCount()?.div(state.config.pageSize)?.plus(1)
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
            }

            val (response, size) = fetch(nextPage)

            saveFetchResult(response, loadType == LoadType.REFRESH)

            MediatorResult.Success(endOfPaginationReached = size < state.config.pageSize)
        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }
}