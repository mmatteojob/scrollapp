package com.matteomacri.scrollapp.domain.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.matteomacri.scrollapp.BuildConfig
import com.matteomacri.scrollapp.domain.vo.DbFavoritePost
import com.matteomacri.scrollapp.domain.vo.DbPost

@Database(
    entities = [
        DbPost::class,
        DbFavoritePost::class
    ],
    version = BuildConfig.VERSION_CODE,
    exportSchema = false
)

@androidx.room.TypeConverters(TypeConverters::class)
abstract class Database : RoomDatabase() {
    abstract fun dao(): Dao
}