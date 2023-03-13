package com.chiore.chiorenews.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chiore.chiorenews.data.model.Article

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article)

    @Query("SELECT * FROM news")
    fun getAllNews(): LiveData<List<Article>>

    @Delete
    suspend fun deleteNews(article: Article)

}