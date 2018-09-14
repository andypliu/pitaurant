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

package com.hanfeng.pitant.data

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.LivePagedListBuilder
import android.util.Log
import com.hanfeng.pitant.api.RestaurantService
import com.hanfeng.pitant.api.searchRestaurant
import com.hanfeng.pitant.db.RestaurantLocalCache
import com.hanfeng.pitant.model.RestaurantSearchResult

/**
 * Repository class that works with local and remote data sources.
 */
class RestaurantRepository(
        private val service: RestaurantService,
        private val cache: RestaurantLocalCache
) {

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    // LiveData of network errors.
    private val networkErrors = MutableLiveData<String>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * Search repositories whose names match the query.
     */
    fun search(query: String): RestaurantSearchResult {
        Log.d("RestaurantRepository",  "New query: $query")
        // lastRequestedPage = 1
        // requestAndSaveData(query)

        // Get data source factory from the local cache
        val dataSourceFactory = cache.restaurantByName(query)

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE).build()

        // Get data from the local cache
        // val data = cache.restaurantByName(query)

        return RestaurantSearchResult(data, networkErrors)
    }

    fun getAllRestaurants(): RestaurantSearchResult {
        Log.d("RestaurantRepository",  "Get all restaurants")

        // Get data source factory from the local cache
        val dataSourceFactory = cache.allRestaurantByName()

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE).build()

        // Get data from the local cache
        // val data = cache.restaurantByName(query)

        return RestaurantSearchResult(data, networkErrors)
    }


    fun requestMore(query: String) {
        requestAndSaveData(query)
    }

    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchRestaurant(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { repos ->
            cache.insert(repos, {
                lastRequestedPage++
                isRequestInProgress = false
            })
        }, { error ->
            networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
        private const val DATABASE_PAGE_SIZE = 20
    }
}