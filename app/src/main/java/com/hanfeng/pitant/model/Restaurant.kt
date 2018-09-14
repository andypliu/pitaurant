package com.hanfeng.pitant.model

import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.hanfeng.pitant.helper.ObservableViewModel

class Restaurant : ObservableViewModel() {
            val id = ObservableInt(0)
            val name = ObservableField("")
            val city = ObservableField("")
            val thumbnail = ObservableField("")
            val type = ObservableField("")
            val rating = ObservableInt(0)
            val address = ObservableField("")
            val phone = ObservableField("")
            val specialDescription = ObservableField("")
            val description = ObservableField("")
            val likes = ObservableInt(0)
            val priceRank = ObservableInt(0)
            val url = ObservableField("")
}