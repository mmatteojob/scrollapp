package com.matteomacri.scrollapp.domain.db

import android.content.Context
import androidx.room.Room
import com.matteomacri.scrollapp.BuildConfig
import com.matteomacri.scrollapp.domain.net.TypeAdapters
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Date
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    @Named("typeAdapters")
    fun typeAdapters(): TypeAdapters = TypeAdapters()

    @Provides
    @Singleton
    @Named("moshi")
    fun moshi(@Named("typeAdapters") typeAdapters: TypeAdapters): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(typeAdapters)
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .build()

    @Provides
    @Singleton
    fun database(
        @ApplicationContext context: Context,
        databaseTypeConverters: TypeConverters
    ): Database =
        Room.databaseBuilder(
            context,
            Database::class.java, BuildConfig.DB_NAME
        )
            .addTypeConverter(databaseTypeConverters)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun dao(database: Database): Dao =
        database.dao()
}