package com.chiore.chiorenews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chiore.chiorenews.R
import com.chiore.chiorenews.data.model.Article
import com.chiore.chiorenews.databinding.BreakingRvItemBinding
import com.chiore.chiorenews.databinding.LatestRvItemBinding
import com.chiore.chiorenews.fragments.news.HomeFragmentDirections

class BreakingNewsRvAdapter() :
    ListAdapter<Article, BreakingNewsRvAdapter.BreakinNewsViewHolder>(DiffUtilCallBack()) {

    inner class BreakinNewsViewHolder(val binding: BreakingRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            with(binding) {
                Glide.with(root).load(article.urlToImage).error(R.drawable.no_image).into(ivBreaking)

                tvBrekingNewsTitle.text = article.title

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreakinNewsViewHolder {
        return BreakinNewsViewHolder(
            BreakingRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BreakinNewsViewHolder, position: Int) {
        val result = getItem(position)
        result?.let {
            holder.bind(result)

            holder.itemView.setOnClickListener { view ->
                val action = HomeFragmentDirections
                    .actionHomeFragmentToDetailsFragment(result)
                Navigation.findNavController(view).navigate(action)
            }

        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

}