package com.chiore.chiorenews.fragments.news

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.chiore.chiorenews.R
import com.chiore.chiorenews.adapters.NewsRvListAdapter
import com.chiore.chiorenews.databinding.FragmentSearchBinding
import com.chiore.chiorenews.util.DefaultItemDecorator
import com.chiore.chiorenews.util.Resource
import com.chiore.chiorenews.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var newsRvListAdapter: NewsRvListAdapter
    private val viewModel by viewModels<SearchViewModel>()

    val TAG = "SearchFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsRvListAdapter = NewsRvListAdapter()

        if (binding.etSearch.requestFocus()) {
            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500)
                editable?.let { query ->
                    if (query.toString().isNotEmpty()) {
                        viewModel.searchNews(query.toString())
                    }
                }
            }
        }

        observeSearchNews()
        searchNewsRv()

    }

    private fun observeSearchNews() {
        lifecycleScope.launch {
            viewModel.searchNews.observe(viewLifecycleOwner) { response ->

                when (response) {
                    is Resource.Loading -> {
                        response.message?.let { message ->
                            Log.e(TAG, "An error occured: $message")
                        }
                    }
                    is Resource.Success -> {
                        response.data?.let { breakingNewsResponse ->
                            newsRvListAdapter.submitList(breakingNewsResponse)
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

    private fun searchNewsRv() {
        binding.searchRv.adapter = newsRvListAdapter
        binding.searchRv.addItemDecoration(
            DefaultItemDecorator(
                resources.getDimensionPixelSize(R.dimen.horizontal_margin_for_vertical),
                resources.getDimensionPixelSize(R.dimen.vertical_margin_for_vertical)
            )
        )
    }
}