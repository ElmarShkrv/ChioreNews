package com.chiore.chiorenews.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn

import com.chiore.chiorenews.data.model.Article
import com.chiore.chiorenews.repository.SearchRepository
import com.chiore.chiorenews.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
) : ViewModel() {

    private val _searchNews = MutableLiveData<Resource<List<Article>>>(Resource.Loading())
    val searchNews: LiveData<Resource<List<Article>>> = _searchNews

    fun searchNews(query: String) {
        viewModelScope.launch {
            val response = repository.searchNews(query)
            response.data?.let {
                _searchNews.value = Resource.Success(it.articles)
            }
        }
    }

}