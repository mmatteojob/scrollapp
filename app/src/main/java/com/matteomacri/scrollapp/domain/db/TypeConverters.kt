package com.matteomacri.scrollapp.domain.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.matteomacri.scrollapp.domain.vo.DbFavoritePost
import com.matteomacri.scrollapp.domain.vo.DbPost
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
@ProvidedTypeConverter
class TypeConverters @Inject constructor(
    @Named("moshi") private val moshi: Moshi
) {
    private val postAdapter = moshi.adapter(DbPost::class.java)
    private val favoritePostAdapter = moshi.adapter(DbFavoritePost::class.java)
    private val favoritePostListAdapter = moshi.adapter<List<DbFavoritePost>>(
        Types.newParameterizedType(
            List::class.java,
            DbFavoritePost::class.java
        )
    )

    @TypeConverter
    fun toPost(value: String?): DbPost? =
        if (value == null) null else try {
            postAdapter.fromJson(value)
        } catch (e: Exception) {
            null
        }

    @TypeConverter
    fun toPostString(value: DbPost?): String? =
        if (value == null) null else try {
            postAdapter.toJson(value)
        } catch (e: Exception) {
            null
        }

    @TypeConverter
    fun toFavoritePost(value: String?): DbFavoritePost? =
        if (value == null) null else try {
            favoritePostAdapter.fromJson(value)
        } catch (e: Exception) {
            null
        }

    @TypeConverter
    fun toFavoritePostString(value: DbFavoritePost?): String? =
        if (value == null) null else try {
            favoritePostAdapter.toJson(value)
        } catch (e: Exception) {
            null
        }

    @TypeConverter
    fun toFavoritePostList(value: String?): List<DbFavoritePost>? =
        if (value == null) null else try {
            favoritePostListAdapter.fromJson(value)
        } catch (e: Exception) {
            null
        }

    @TypeConverter
    fun toFavoritePostListString(value: List<DbFavoritePost>?): String? =
        if (value == null) null else try {
            favoritePostListAdapter.toJson(value)
        } catch (e: Exception) {
            null
        }

    @TypeConverter
    fun fromList(list: List<String>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toList(data: String?): List<String> {
        return data?.split(",") ?: emptyList()
    }
}


