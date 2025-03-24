package com.matteomacri.scrollapp.domain.net

import com.matteomacri.scrollapp.BuildConfig
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type
import java.util.Date
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetModule {

    @Provides
    @Singleton
    @Named("libBaseUrl")
    fun provideLibBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun typeAdapters(): TypeAdapters = TypeAdapters()

    @Provides
    @Singleton
    fun moshi(typeAdapters: TypeAdapters): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(typeAdapters)
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .add(object : JsonAdapter.Factory {
                override fun create(
                    type: Type,
                    annotations: Set<Annotation>,
                    moshi: Moshi
                ): JsonAdapter<*>? {
                    val jsonAdapter = moshi.nextAdapter<Any>(this, type, annotations)
                    return object : JsonAdapter<Any?>() {
                        override fun fromJson(reader: JsonReader): Any? {
                            reader.isLenient = true  // Enable lenient mode here
                            return jsonAdapter.fromJson(reader)
                        }

                        override fun toJson(writer: JsonWriter, value: Any?) {
                            jsonAdapter.toJson(writer, value)
                        }
                    }
                }
            })
            .build()

    @Provides
    @Singleton
    @Named("log")
    fun logInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    @Named("language")
    fun provideLanguageInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val modifiedRequest = originalRequest.newBuilder()
                .header("Accept-Language", "en")
                .build()
            chain.proceed(modifiedRequest)
        }
    }

    @Provides
    @Singleton
    fun httpClient(
        @Named("log") logInterceptor: Interceptor,
        @Named("language") languageInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(languageInterceptor)
            .addInterceptor(logInterceptor)
            .build()

    @Provides
    @Singleton
    @Named("moshi")
    fun moshiConverterFactory(moshi: Moshi): Converter.Factory =
        MoshiConverterFactory.create(moshi)

    @Provides
    @Singleton
    fun retrofit(
        okHttpClient: OkHttpClient,
        @Named("moshi") moshiConverterFactory: Converter.Factory,
        @Named("libBaseUrl") baseUrl: String
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(moshiConverterFactory)
            .client(okHttpClient)
            .build()


    @Provides
    @Singleton
    fun api(retrofit: Retrofit): Api =
        retrofit.create(Api::class.java)
}