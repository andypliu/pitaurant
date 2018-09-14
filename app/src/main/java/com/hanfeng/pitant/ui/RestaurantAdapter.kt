package com.hanfeng.pitant.ui

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.hanfeng.pitant.db.RestaurantEntity

/**
 * Adapter for the list of repositories.
 */
class RestaurantAdapter : PagedListAdapter<RestaurantEntity, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RestaurantViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as RestaurantViewHolder).bind(repoItem)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<RestaurantEntity>() {
            override fun areItemsTheSame(oldItem: RestaurantEntity, newItem: RestaurantEntity): Boolean =
                    oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: RestaurantEntity, newItem: RestaurantEntity): Boolean =
                    oldItem == newItem
        }
    }
}