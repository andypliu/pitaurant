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
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

/**
 * Room data access object for accessing the [RestaurantEntity] table.
 */
@Dao
interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<RestaurantEntity>)

    @Query("SELECT * FROM restaurants ORDER BY name COLLATE NOCASE ASC")
    fun allRestaurantByName(): DataSource.Factory<Int, RestaurantEntity>

    // Do a similar query as the search API:
    // Look for restaurants that contain the query string in the name or in the description
    // and order those results descending, by the number of stars and then by name
    @Query("SELECT * FROM restaurants WHERE (name LIKE :queryString) OR (description LIKE " +
            ":queryString) ORDER BY rating DESC, name ASC")
    fun restaurantByName(queryString: String): DataSource.Factory<Int, RestaurantEntity>

}