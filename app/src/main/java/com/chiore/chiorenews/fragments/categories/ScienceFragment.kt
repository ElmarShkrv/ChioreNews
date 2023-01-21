package com.chiore.chiorenews.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.chiore.chiorenews.viewmodel.categoryviewmodels.BaseCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScienceFragment: BaseCategoryFragment() {

    private val viewModel by viewModels<BaseCategoryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.latestNewsByScience.observe(viewLifecycleOwner) { scienceNewsResponse ->
            latestNewsRvAdapter.submitData(viewLifecycleOwner.lifecycle, scienceNewsResponse)
        }
    }
}