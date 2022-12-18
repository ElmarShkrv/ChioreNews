package com.chiore.chiorenews.viewmodel.categoryviewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiore.chiorenews.data.model.Article
import com.chiore.chiorenews.repository.MainCategoryRepository
import com.chiore.chiorenews.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val repository: MainCategoryRepository,
) : ViewModel() {

    private val _breakingNews = MutableLiveData<Resource<List<Article>>>(Resource.Loading())
    val breakingNews: LiveData<Resource<List<Article>>> = _breakingNews

    fun breakingNews() {
        viewModelScope.launch {
            val response = repository.breakingNews()
            response.data?.let {
                _breakingNews.value = Resource.Success(it.articles)
            }
        }
    }

}