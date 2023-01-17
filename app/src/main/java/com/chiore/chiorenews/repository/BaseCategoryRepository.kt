package com.chiore.chiorenews.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.chiore.chiorenews.data.model.NewsResponse
import com.chiore.chiorenews.data.remote.NewsApi
import com.chiore.chiorenews.paging.LatestNewsByCategoryPagingSource
import com.chiore.chiorenews.paging.LatestNewsPagingSource
import com.chiore.chiorenews.util.Resource
import javax.inject.Inject

class BaseCategoryRepository @Inject constructor(
    private val newsApi: NewsApi,
) {
    fun getLatestNewsByCategory(category: String) =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { LatestNewsByCategoryPagingSource(newsApi, category) }
        ).liveData
}