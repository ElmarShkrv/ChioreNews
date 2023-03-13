package com.chiore.chiorenews.repository

import com.chiore.chiorenews.data.local.NewsDao
import com.chiore.chiorenews.data.model.Article
import javax.inject.Inject

class ReadLaterRepository @Inject constructor(
    private val newsDao: NewsDao,
) {

    suspend fun upsert(article: Article) = newsDao.upsert(article)

    fun getSavedNews() = newsDao.getAllNews()

    suspend fun deleteNews(article: Article) = newsDao.deleteNews(article)

}