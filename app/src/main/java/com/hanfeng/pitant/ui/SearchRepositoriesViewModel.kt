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

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.Context
import com.hanfeng.pitant.data.RestaurantRepository
import com.hanfeng.pitant.db.RestaurantDatabase
import com.hanfeng.pitant.db.RestaurantEntity
import com.hanfeng.pitant.model.RestaurantSearchResult

/**
 * ViewModel for the [RestaurantHome] screen.
 * The ViewModel works with the [RestaurantRepository] to get the data.
 */
class SearchRepositoriesViewModel(private val repository: RestaurantRepository, private val context : Context) : ViewModel() {

    companion object {
        private const val VISIBLE_THRESHOLD = 7

        /**
         * A good page size is a value that fills at least a screen worth of content on a large
         * device so the User is unlikely to see a null item.
         * You can play with this constant to observe the paging behavior.
         * <p>
         * It's possible to vary this with list device size, but often unnecessary, unless a user
         * scrolling on a large device is expected to scroll through items more quickly than a small
         * device, such as when the large device uses a grid layout of items.
         */
        private const val PAGE_SIZE = 20

        /**
         * If placeholders are enabled, PagedList will report the full size but some items might
         * be null in onBind method (PagedListAdapter triggers a rebind when data is loaded).
         * <p>
         * If placeholders are disabled, onBind will never receive null but as more pages are
         * loaded, the scrollbars will jitter as new pages are loaded. You should probably disable
         * scrollbars if you disable placeholders.
         */
        private const val ENABLE_PLACEHOLDERS = true
    }

    private val dao = RestaurantDatabase.getInstance(context).restaurantDao()

    private val queryLiveData = MutableLiveData<String>()
    private val restaurantResult: LiveData<RestaurantSearchResult> = Transformations.map(queryLiveData, {
        repository.search(it)
    })

    val restaurants: LiveData<PagedList<RestaurantEntity>> = Transformations.switchMap(restaurantResult,
            { it -> it.data })
    val networkErrors: LiveData<String> = Transformations.switchMap(restaurantResult,
            { it -> it.networkErrors })

    val allRestaurants = LivePagedListBuilder(dao.allRestaurantByName(), PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(ENABLE_PLACEHOLDERS)
            .build()).build()

    /**
     * Search a repository based on a query string.
     */
    fun searchRestaurant(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            val immutableQuery = lastQueryValue()
            if (immutableQuery != null) {
                repository.requestMore(immutableQuery)
            }
        }
    }

    /**
     * Get the last query value.
     */
    fun lastQueryValue(): String? = queryLiveData.value
}