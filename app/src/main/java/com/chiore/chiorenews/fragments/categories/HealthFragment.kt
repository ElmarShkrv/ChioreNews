package com.chiore.chiorenews.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.chiore.chiorenews.viewmodel.categoryviewmodels.BaseCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HealthFragment: BaseCategoryFragment() {

    private val viewModel by viewModels<BaseCategoryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.latestNewsByHealth.observe(viewLifecycleOwner) { healthNewsResponse ->
            latestNewsRvAdapter.submitData(viewLifecycleOwner.lifecycle, healthNewsResponse)
        }
    }
}