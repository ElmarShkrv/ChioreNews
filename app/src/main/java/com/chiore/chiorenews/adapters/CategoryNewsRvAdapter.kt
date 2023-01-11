package com.chiore.chiorenews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chiore.chiorenews.data.model.Article
import com.chiore.chiorenews.databinding.LatestRvItemBinding

class CategoryNewsRvAdapter() :
    ListAdapter<Article, CategoryNewsRvAdapter.CategoryNewsViewHolder>(DiffUtilCallBack()) {

    inner class CategoryNewsViewHolder(val binding: LatestRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            with(binding) {
                Glide.with(root).load(article.urlToImage).into(ivLatest)

                tvTitle.text = article.title
                tvAuthor.text = article.author
                tvPublishedAt.text = article.publishedAt

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryNewsViewHolder {
        return CategoryNewsViewHolder(
            LatestRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryNewsViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)
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