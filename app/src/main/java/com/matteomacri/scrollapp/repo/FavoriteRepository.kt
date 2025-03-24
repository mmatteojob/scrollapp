package com.matteomacri.scrollapp.repo

import com.matteomacri.scrollapp.domain.db.Dao
import com.matteomacri.scrollapp.domain.vo.DbFavoritePost
import com.matteomacri.scrollapp.domain.vo.DbPost
import com.matteomacri.scrollapp.ui.core.utils.Resource
import com.matteomacri.scrollapp.ui.core.utils.networkBoundResource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface FavoriteRepository {
    fun getFavorites(): Flow<Resource<List<DbFavoritePost>>>
    fun isFavorite(postId: Int): Flow<Resource<Boolean>>
    suspend fun addPostToFavorites(post: DbPost)
    suspend fun deletePostFromFavorites(postId: Int)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteModule {
    @Binds
    @Singleton
    abstract fun provideFavoriteRepository(favoriteRepositoryImpl: FavoriteRepositoryImpl): FavoriteRepository
}

@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val dao: Dao
) : FavoriteRepository {

    override fun getFavorites() = networkBoundResource(
        fetch = {},
        query = { dao.getFavorites() },
        saveFetchResult = {}
    )

    override fun isFavorite(postId: Int) = networkBoundResource(
        fetch = {},
        query = { dao.isFavorite(postId) },
        saveFetchResult = {}
    )

    override suspend fun addPostToFavorites(post: DbPost) {
        dao.addPostToFavorites(
            DbFavoritePost(
                postId = post.id,
                post = post
            )
        )
    }

    override suspend fun deletePostFromFavorites(postId: Int) {
        dao.deletePostFromFavorites(postId)
    }
}
