package com.matteomacri.scrollapp.domain.net

import com.matteomacri.scrollapp.domain.vo.NetPost
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("posts")
    suspend fun getPosts(
        @Query("_page") page: Int,
        @Query("_per_page") perPage: Int
    ): List<NetPost>

    @GET("posts/{id}")
    suspend fun getPost(
        @Path("id") id: Int
    ): NetPost

}