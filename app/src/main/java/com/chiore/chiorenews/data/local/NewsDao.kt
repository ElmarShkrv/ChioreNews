package com.chiore.chiorenews.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.chiore.chiorenews.data.model.Article

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM news")
    fun getAllNews(): LiveData<List<Article>>

    @Delete
    suspend fun deleteNews(article: Article)

}