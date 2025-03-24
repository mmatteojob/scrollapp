package com.matteomacri.scrollapp.domain.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matteomacri.scrollapp.domain.vo.DbFavoritePost
import com.matteomacri.scrollapp.domain.vo.DbPost
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    // INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<DbPost>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: DbPost)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPostToFavorites(favorite: DbFavoritePost)

    // SELECT
    @Query("SELECT * FROM post ORDER BY id ASC")
    fun getPostsPaged(): PagingSource<Int, DbPost>

    @Query("SELECT count(*) FROM post")
    suspend fun getPostsCount(): Int

    @Query("SELECT * FROM post WHERE id =:postId")
    fun getPost(postId: Int): Flow<DbPost>

    @Query("SELECT * FROM favorites")
    fun getFavorites(): Flow<List<DbFavoritePost>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE postId=:postId)")
    fun isFavorite(postId: Int): Flow<Boolean>

    // DELETE
    @Query("DELETE FROM post")
    suspend fun deletePosts()

    @Query("DELETE FROM favorites WHERE postId = :postId")
    suspend fun deletePostFromFavorites(postId: Int)

    @Query("DELETE FROM post WHERE id = :postId")
    suspend fun deletePost(postId: Int)
}