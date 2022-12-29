package com.chiore.chiorenews.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.chiore.chiorenews.R
import com.chiore.chiorenews.adapters.latestNewsRvAdapter
import com.chiore.chiorenews.adapters.BreakingNewsRvAdapter
import com.chiore.chiorenews.databinding.FragmentMainCategoryBinding
import com.chiore.chiorenews.util.DefaultItemDecorator
import com.chiore.chiorenews.util.Resource
import com.chiore.chiorenews.viewmodel.categoryviewmodels.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {

    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var breakingNewsRvAdapter: BreakingNewsRvAdapter
    private lateinit var latestNewsRvAdapter: latestNewsRvAdapter
    private val viewModel: MainCategoryViewModel by viewModels()

    val TAG = "MainCategoryFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        breakingNewsRvAdapter = BreakingNewsRvAdapter()
        latestNewsRvAdapter = latestNewsRvAdapter()

        viewModel.breakingNews()
        observeBreakingNews()
        setupBreakingNewsRv()

        viewModel.latestNews.observe(viewLifecycleOwner) { allNewsResponse ->
            latestNewsRvAdapter.submitData(viewLifecycleOwner.lifecycle, allNewsResponse)
        }
        latestNewsRv()

    }

    private fun latestNewsRv() {
        binding.latestNewsRv.adapter = latestNewsRvAdapter
        binding.latestNewsRv.addItemDecoration(DefaultItemDecorator(
            resources.getDimensionPixelSize(R.dimen.horizontal_margin_for_vertical),
            resources.getDimensionPixelSize(R.dimen.vertical_margin_for_vertical)
        ))
    }

    private fun setupBreakingNewsRv() {
        binding.breakingNewsRv.adapter = breakingNewsRvAdapter
    }

    private fun observeBreakingNews() {
        lifecycleScope.launch {
            viewModel.breakingNews.observe(viewLifecycleOwner) { response ->

                when (response) {
                    is Resource.Loading -> {
                        response.message?.let { message ->
                            Log.e(TAG, "An error occured: $message")
                        }
                    }
                    is Resource.Success -> {
                        response.data?.let { breakingNewsResponse ->
                            breakingNewsRvAdapter.submitList(breakingNewsResponse)
                        }
                    }
                    is Resource.Error -> {
                        response.message?.let { message ->
                            Log.e(TAG, "An error occured: $message")
                        }
                    }
                    else -> Unit
                }

            }

        }
    }

}