package com.chiore.chiorenews.viewmodel.categoryviewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiore.chiorenews.data.model.Article
import com.chiore.chiorenews.util.Resource
import com.chiore.chiorenews.util.repository.BaseCategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaseCategoryViewModel @Inject constructor(
    private val repository: BaseCategoryRepository,
): ViewModel() {

    private val _categoryNews = MutableLiveData<Resource<List<Article>>>(Resource.Loading())
    val categoryNews: LiveData<Resource<List<Article>>> = _categoryNews

    fun getNewsByCategory(category: String) {
        viewModelScope.launch {
            val response = repository.getNewsByCategory(category)
            response.data?.let {
                _categoryNews.value = Resource.Success(it.articles)
            }
        }
    }

}