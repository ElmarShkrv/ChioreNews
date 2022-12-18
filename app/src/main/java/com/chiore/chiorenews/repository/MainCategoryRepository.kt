package com.chiore.chiorenews.repository

import com.chiore.chiorenews.data.model.NewsResponse
import com.chiore.chiorenews.data.remote.NewsApi
import com.chiore.chiorenews.util.Resource
import javax.inject.Inject

class MainCategoryRepository @Inject constructor(
    private val newsApi: NewsApi,
) {
    suspend fun breakingNews(): Resource<NewsResponse> {
        return try {
            val response = newsApi.getBreakingNews()
            if (response.isSuccessful) {
                response.body()?.let {
                    return Resource.Success(it)
                } ?: Resource.Error("Response is null")
            } else {
                Resource.Error("Response is not successful")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}