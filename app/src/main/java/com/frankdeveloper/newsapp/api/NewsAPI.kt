package com.frankdeveloper.newsapp.api

import com.frankdeveloper.newsapp.models.PostResponse
import retrofit2.Response
import retrofit2.http.GET

interface NewsAPI {

    @GET("api/v1/search_by_date?query=mobile")
    suspend fun getBreakingNews(): Response<PostResponse>
}