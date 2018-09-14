package com.hanfeng.pitant.ui

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.hanfeng.pitant.R

class MainActivity : AppCompatActivity(), RestaurantDetailFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.d("MainActivity", "Interact with fragment: ${uri.toString()}")
    }
}