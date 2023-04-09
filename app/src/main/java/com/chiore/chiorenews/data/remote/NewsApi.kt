package com.chiore.chiorenews.data.remote


import com.chiore.chiorenews.BuildConfig.API_KEY
import com.chiore.chiorenews.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode: String = "us",
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY,
    ): Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getLatestNewsByCategory(
        @Query("country") countryCode: String = "us",
        @Query("category") category: String,
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY,
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun getLatestNews(
        @Query("q") searchQuery: String = "a",
        @Query("page") pageNumber: Int,
        @Query("apiKey") apiKey: String = API_KEY,
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun getSearchNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY,
    ): Response<NewsResponse>

}