package com.chiore.chiorenews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chiore.chiorenews.data.model.Article
import com.chiore.chiorenews.databinding.BreakingRvItemBinding

class BreakingNewsRvAdapter() :
    ListAdapter<Article, BreakingNewsRvAdapter.BreakinNewsViewHolder>(DiffUtilCallBack()) {

    inner class BreakinNewsViewHolder(val binding: BreakingRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(article: Article) {
                with(binding) {
                    Glide.with(root).load(article.urlToImage).into(ivPopular)
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