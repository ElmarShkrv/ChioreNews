package com.chiore.chiorenews.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiore.chiorenews.data.model.Article
import com.chiore.chiorenews.repository.DetailsRepository
import com.chiore.chiorenews.repository.ReadLaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadLaterViewModel @Inject constructor(
    private val repository: ReadLaterRepository,
) : ViewModel() {

    fun saveNews(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    fun getSavedNews() = repository.getSavedNews()

    fun deleteNews(article: Article) = viewModelScope.launch {
        repository.deleteNews(article)
    }

}