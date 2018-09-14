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

package com.hanfeng.pitant.api

import android.util.Log
import com.hanfeng.pitant.db.RestaurantEntity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val TAG = "RestaurantService"
private const val IN_QUALIFIER = "in:name,description"

/**
 * Search restaurants based on a query.
 * Trigger a request to the Github searchRestaurant API with the following params:
 * @param query searchRestaurant keyword
 * @param page request page index
 * @param itemsPerPage number of repositories to be returned by the Github API per page
 *
 * The result of the request is handled by the implementation of the functions passed as params
 * @param onSuccess function that defines how to handle the list of restaurants received
 * @param onError function that defines how to handle request failure
 */
fun searchRestaurant(
        service: RestaurantService,
        query: String,
        page: Int,
        itemsPerPage: Int,
        onSuccess: (restaurantEntities: List<RestaurantEntity>) -> Unit,
        onError: (error: String) -> Unit) {
    Log.d(TAG, "query: $query, page: $page, itemsPerPage: $itemsPerPage")

    val apiQuery = query + IN_QUALIFIER

    //TODO: Query Parse Server
    /*
    service.searchRestaurant(apiQuery, page, itemsPerPage).enqueue(
            object : Callback<RestaurantSearchResponse> {
                override fun onFailure(call: Call<RestaurantSearchResponse>?, t: Throwable) {
                    Log.d(TAG, "fail to get data")
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                        call: Call<RestaurantSearchResponse>?,
                        response: Response<RestaurantSearchResponse>
                ) {
                    Log.d(TAG, "got a response $response")
                    if (response.isSuccessful) {
                        val restaurants = response.body()?.items ?: emptyList()
                        onSuccess(restaurants)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
    */
}

/**
 * Github API communication setup via Retrofit.
 */
interface RestaurantService {
    /**
     * Get restaurants ordered by stars.
     */
    @GET("search/repositories?sort=stars")
    fun searchRestaurant(@Query("q") query: String,
                         @Query("page") page: Int,
                         @Query("per_page") itemsPerPage: Int): Call<RestaurantSearchResponse>


    companion object {
        private const val BASE_URL = "https://api.github.com/"

        fun create(): RestaurantService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RestaurantService::class.java)
        }
    }
}