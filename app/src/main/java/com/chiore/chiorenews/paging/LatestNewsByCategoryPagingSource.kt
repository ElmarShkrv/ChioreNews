package com.chiore.chiorenews.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.chiore.chiorenews.data.model.Article
import com.chiore.chiorenews.data.remote.NewsApi
import com.chiore.chiorenews.util.Constants.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

class LatestNewsByCategoryPagingSource(
    private val newsApi: NewsApi,
    private val category: String
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val position = params.key ?: STARTING_PAGE_INDEX
            val response = newsApi.getLatestNewsByCategory(pageNumber = position, category = category)
            val news = response.body()!!.articles

            LoadResult.Page(data = news,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (news.isEmpty()) null else position + 1)

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}