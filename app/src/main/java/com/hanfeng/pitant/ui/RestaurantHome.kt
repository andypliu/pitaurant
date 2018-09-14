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

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ListView
import android.widget.Toast
import com.hanfeng.pitant.R
import com.hanfeng.pitant.helper.Injection
import com.hanfeng.pitant.db.RestaurantEntity
import com.hanfeng.pitant.model.Restaurant
import kotlinx.android.synthetic.main.fragment_restaurant_home.*


class RestaurantHome : Fragment() {

    private lateinit var viewModel: SearchRepositoriesViewModel
    private val adapter = RestaurantAdapter()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurant_home, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // get the view model
        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this.context!!))
                .get(SearchRepositoriesViewModel::class.java)

        setupScrollListener()

        initAdapter()
      //  val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
      //  viewModel.searchRestaurant(query)

       // initSearch(query)

        viewModel.allRestaurants.observe(this, Observer(adapter::submitList))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
    }

    private fun initAdapter() {
        list.adapter = adapter
        /*
        viewModel.restaurants.observe(this, Observer<PagedList<RestaurantEntity>> {
            Log.d("Activity", "list: ${it?.size}")
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
        })
        */

        viewModel.allRestaurants.observe(this, Observer<PagedList<RestaurantEntity>> {
            Log.d("RestaurantHome", "list all restaurant: ${it?.size}")
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
        })

        viewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(this.context!!, "\uD83D\uDE28 Wooops ${it}", Toast.LENGTH_LONG).show()
        })
    }

    private fun initSearch(query: String) {
        search_repo.setText(query)

        search_repo.setOnEditorActionListener({ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRestaurantListFromInput()
                true
            } else {
                false
            }
        })
        search_repo.setOnKeyListener({ _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRestaurantListFromInput()
                true
            } else {
                false
            }
        })
    }

    private fun updateRestaurantListFromInput() {
        search_repo.text.trim().let {
            if (it.isNotEmpty()) {
                list.scrollToPosition(0)
                viewModel.searchRestaurant(it.toString())
                adapter.submitList(null)
            }
        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            list.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            list.visibility = View.VISIBLE
        }
    }

    private fun setupScrollListener() {
        val layoutManager = list.layoutManager as LinearLayoutManager
        layoutManager.initialPrefetchItemCount = 20
        layoutManager.isSmoothScrollbarEnabled = true

        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Android"
    }
}
