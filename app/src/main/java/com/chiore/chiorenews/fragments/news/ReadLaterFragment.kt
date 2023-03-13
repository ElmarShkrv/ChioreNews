package com.chiore.chiorenews.fragments.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chiore.chiorenews.R
import com.chiore.chiorenews.adapters.NewsRvListAdapter
import com.chiore.chiorenews.data.model.Article
import com.chiore.chiorenews.databinding.FragmentReadLaterBinding
import com.chiore.chiorenews.util.DefaultItemDecorator
import com.chiore.chiorenews.viewmodel.ReadLaterViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadLaterFragment : Fragment() {

    private lateinit var binding: FragmentReadLaterBinding
    private lateinit var newsRvListAdapter: NewsRvListAdapter
    private val viewModel by viewModels<ReadLaterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentReadLaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getNews()
        setupReadLaterRv()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.layoutPosition

                val deleteNews = newsRvListAdapter.currentList[position]
                val insertNews = newsRvListAdapter.currentList[position]

                viewModel.deleteNews(deleteNews)
                newsRvListAdapter.notifyItemRemoved(position)
//                newsRvListAdapter.notifyDataSetChanged()
//                newsRvListAdapter.notifyItemRemoved(viewHolder.adapterPosition)

                Snackbar.make(requireView(), "News deleted", Snackbar.LENGTH_SHORT).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.saveNews(insertNews)
                    }
                ).show()
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.readLaterRv)

    }

    private fun setupReadLaterRv() {
        newsRvListAdapter = NewsRvListAdapter()
        binding.apply {
            readLaterRv.adapter = newsRvListAdapter
            readLaterRv.addItemDecoration(
                DefaultItemDecorator(
                    resources.getDimensionPixelSize(R.dimen.horizontal_margin_for_vertical),
                    resources.getDimensionPixelSize(R.dimen.vertical_margin_for_vertical)
                )
            )
        }
    }

    private fun getNews() {
        viewModel.getSavedNews().observe(viewLifecycleOwner) { news ->
            newsRvListAdapter.submitList(news)
        }
    }

}