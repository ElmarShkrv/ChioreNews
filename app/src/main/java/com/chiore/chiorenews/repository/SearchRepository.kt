package com.chiore.chiorenews.repository


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.chiore.chiorenews.data.model.NewsResponse
import com.chiore.chiorenews.data.remote.NewsApi
import com.chiore.chiorenews.util.Resource
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val newsApi: NewsApi,
) {

    suspend fun searchNews(query: String): Resource<NewsResponse> {
        return try {
            val response = newsApi.getSearchNews(query)
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