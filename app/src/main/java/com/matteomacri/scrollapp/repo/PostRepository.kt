package com.matteomacri.scrollapp.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.matteomacri.scrollapp.domain.db.Dao
import com.matteomacri.scrollapp.domain.db.Database
import com.matteomacri.scrollapp.domain.net.Api
import com.matteomacri.scrollapp.domain.vo.DbPost
import com.matteomacri.scrollapp.domain.vo.toDbPost
import com.matteomacri.scrollapp.ui.core.utils.Resource
import com.matteomacri.scrollapp.ui.core.utils.networkBoundResource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface PostRepository {
    fun getPosts(): Flow<PagingData<DbPost>>

    fun getPost(id: Int): Flow<Resource<DbPost>>
}

@Module
@InstallIn(SingletonComponent::class)
abstract class PostModule {
    @Binds
    @Singleton
    abstract fun providePostRepository(postRepositoryImpl: PostRepositoryImpl): PostRepository
}

@OptIn(ExperimentalPagingApi::class)
@Singleton
class PostRepositoryImpl @Inject constructor(
    private val api: Api,
    private val dao: Dao,
    private val database: Database,
) : PostRepository {

    companion object {
        const val PAGE_SIZE = 10
    }

    override fun getPosts() = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE
        ),
        remoteMediator = GenericRemoteMediator(
            getLocalItemCount = { dao.getPostsCount() },
            fetch = { page ->
                val response = api.getPosts(page, PAGE_SIZE)
                response to response.size
            },
            saveFetchResult = { posts, clearLocalItems ->
                database.withTransaction {
                    if (clearLocalItems) {
                        dao.deletePosts()
                    }
                    dao.insertPosts(posts.map { it.toDbPost() })
                }
            }
        )
    ) {
        dao.getPostsPaged()
    }
        .flow

    override fun getPost(id: Int) = networkBoundResource(
        fetch = { api.getPost(id) },
        query = { dao.getPost(id) },
        saveFetchResult = { response ->
            database.withTransaction {
                dao.deletePost(response.id)
                dao.insertPost(response.toDbPost())
            }
        }
    )
}
