package com.chiore.chiorenews.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chiore.chiorenews.R
import com.chiore.chiorenews.adapters.CategoryNewsRvAdapter
import com.chiore.chiorenews.databinding.FragmentBaseCategoryBinding
import com.chiore.chiorenews.util.DefaultItemDecorator

open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {
    private lateinit var binding: FragmentBaseCategoryBinding
    protected val categoryNewsRvAdapter: CategoryNewsRvAdapter by lazy { CategoryNewsRvAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLatestNewsRv()
    }

    private fun setupLatestNewsRv() {
        binding.baseCategoryRv.adapter = categoryNewsRvAdapter
        binding.baseCategoryRv.addItemDecoration(DefaultItemDecorator(
            resources.getDimensionPixelSize(R.dimen.horizontal_margin_for_vertical),
            resources.getDimensionPixelSize(R.dimen.vertical_margin_for_vertical)
        ))
    }
}