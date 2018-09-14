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

package com.hanfeng.pitant.db

import android.arch.paging.DataSource
import android.util.Log
import java.util.concurrent.Executor

/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 */
class RestaurantLocalCache(
        private val restaurantDao: RestaurantDao,
        private val ioExecutor: Executor
) {

    /**
     * Insert a list of restaurantEntities in the database, on a background thread.
     */
    fun insert(restaurantEntities: List<RestaurantEntity>, insertFinished: ()-> Unit) {
        ioExecutor.execute {
            Log.d("RestaurantLocalCache", "inserting ${restaurantEntities.size} restaurantEntities")
            restaurantDao.insert(restaurantEntities)
            insertFinished()
        }
    }

    /**
     * Request a LiveData<List<RestaurantEntity>> from the Dao, based on a repo name. If the name contains
     * multiple words separated by spaces, then we're emulating the GitHub API behavior and allow
     * any characters between the words.
     * @param name repository name
     */
    fun restaurantByName(name: String): DataSource.Factory<Int, RestaurantEntity> {
        // appending '%' so we can allow other characters to be before and after the query string
        val query = "%${name.replace(' ', '%')}%"
        return restaurantDao.restaurantByName(query)
    }

    fun allRestaurantByName(): DataSource.Factory<Int, RestaurantEntity> {
        return restaurantDao.allRestaurantByName()
    }
}