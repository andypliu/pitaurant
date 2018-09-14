/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hanfeng.pitant.ui

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.hanfeng.pitant.R
import com.hanfeng.pitant.db.RestaurantEntity
import com.hanfeng.pitant.helper.BindingAdapters
import kotlinx.android.synthetic.main.restaurant_view_item.view.*

/**
 * View Holder for a [RestaurantEntity] RecyclerView list item.
 */
class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.tv_name)
    private val thumbnail: ImageView = view.findViewById(R.id.iv_thumbnail)
   // private val description: TextView = view.findViewById(R.id.tv_distant)
    private val rating: ImageView = view.findViewById(R.id.iv_rating)
    private val city: TextView = view.findViewById(R.id.tv_city)
    private val type: ImageView = view.findViewById(R.id.iv_type)

    private var restaurantEntity: RestaurantEntity? = null

    init {
        view.setOnClickListener {
            /*
            restaurantEntity?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
            */
            Log.d("RestaurantViewHolder", "Go view detail: ${it.tv_name.text}")

            val bundle = Bundle()
            bundle.putString("name", name.text.toString())
            bundle.putParcelable("restaurantEntity", this.restaurantEntity)

            Navigation.findNavController(view).navigate(
                    R.id.action_restaurantHome_to_restaurantDetailFragment,
                    bundle
            )
        }
    }

    fun bind(restaurantEntity: RestaurantEntity?) {
        if (restaurantEntity == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            //description.visibility = View.GONE
            city.visibility = View.GONE
            //rating.text = resources.getString(R.string.unknown)
            //type.text = resources.getString(R.string.unknown)
        } else {
            showRestaurantData(restaurantEntity)
        }
    }

    private fun showRestaurantData(restaurantEntity: RestaurantEntity) {
        this.restaurantEntity = restaurantEntity
        name.text = restaurantEntity.name
        Log.d("RestaurantViewHolder", "Name: ${name.text}")

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            val resourceId = itemView!!.context.resources.getIdentifier(restaurantEntity.thumbnail, "drawable", itemView!!.context.packageName)

            thumbnail.setImageDrawable(itemView!!.context.getDrawable(resourceId))
            rating.setImageDrawable(BindingAdapters.getRatingDrawable(restaurantEntity.rating, itemView.context))
            type.setImageDrawable(itemView!!.context.getDrawable(R.drawable.yue)) // restaurantEntity.likes.toString()

        } else {
            // handle lower version later
        }

        // if the city is missing, hide the label and the value
        var languageVisibility = View.GONE
        if (!restaurantEntity.specialDescription.isNullOrEmpty()) {
            val resources = this.itemView.context.resources
            city.text = restaurantEntity.city
            languageVisibility = View.VISIBLE
        }
        city.visibility = languageVisibility
    }

    companion object {
        fun create(parent: ViewGroup): RestaurantViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.restaurant_view_item, parent, false)
            return RestaurantViewHolder(view)
        }
    }
}