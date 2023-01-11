package com.chiore.chiorenews.util.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.chiore.chiorenews.data.model.NewsResponse
import com.chiore.chiorenews.data.remote.NewsApi
import com.chiore.chiorenews.paging.LatestNewsPagingSource
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

    fun getLatestNews() =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { LatestNewsPagingSource(newsApi) }
        ).liveData
}