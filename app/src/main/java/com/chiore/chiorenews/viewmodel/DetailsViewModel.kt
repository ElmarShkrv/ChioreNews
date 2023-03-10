package com.chiore.chiorenews.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiore.chiorenews.data.model.Article
import com.chiore.chiorenews.repository.DetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: DetailsRepository,
) : ViewModel() {

    private val _isFavorite = MutableLiveData<Boolean>(false)
    var isFavorite = _isFavorite.value

    fun saveNews(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    fun deleteNews(article: Article) = viewModelScope.launch {
        repository.deleteNews(article)
    }

}