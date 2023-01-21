package com.chiore.chiorenews.viewmodel.categoryviewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.chiore.chiorenews.repository.BaseCategoryRepository
import com.chiore.chiorenews.util.Categories
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BaseCategoryViewModel @Inject constructor(
    private val repository: BaseCategoryRepository,
) : ViewModel() {

    val latestNewsByBusiness = repository.getLatestNewsByCategory(Categories.Business.category).cachedIn(viewModelScope)
    val latestNewsByEntertainment = repository.getLatestNewsByCategory(Categories.Entertainment.category).cachedIn(viewModelScope)
    val latestNewsByHealth = repository.getLatestNewsByCategory(Categories.Health.category).cachedIn(viewModelScope)
    val latestNewsByScience = repository.getLatestNewsByCategory(Categories.Science.category).cachedIn(viewModelScope)
    val latestNewsBySports = repository.getLatestNewsByCategory(Categories.Sports.category).cachedIn(viewModelScope)
    val latestNewsByTechnology = repository.getLatestNewsByCategory(Categories.Technology.category).cachedIn(viewModelScope)

}