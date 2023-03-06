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
import com.chiore.chiorenews.fragments.news.SearchFragmentDirections

class SearchNewsRvAdapter() :
    ListAdapter<Article, SearchNewsRvAdapter.SearchNewsViewHolder>(DiffUtilCallBack()) {

    inner class SearchNewsViewHolder(val binding: LatestRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            with(binding) {
                Glide.with(root).load(article.urlToImage).error(R.drawable.no_image).into(ivLatest)

                tvTitle.text = article.title
                tvAuthor.text = article.author
                tvPublishedAt.text = article.publishedAt

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchNewsViewHolder {
        return SearchNewsViewHolder(
            LatestRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: SearchNewsViewHolder, position: Int) {
        val result = getItem(position)
        result?.let {
            holder.bind(result)

            holder.itemView.setOnClickListener { view ->
                val action = SearchFragmentDirections
                    .actionSearchFragmentToDetailsFragment(result)
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