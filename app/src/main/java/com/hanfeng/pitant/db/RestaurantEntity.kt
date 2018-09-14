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

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hanfeng.pitant.helper.ObservableViewModel

/**
 * Immutable model class for a Github repo that holds all the information about a repository.
 * Objects of this type are received from the Github API, therefore all the fields are annotated
 * with the serialized name.
 * This class also defines the Room restaurants table, where the repo [id] is the primary key.
 */
@Entity(tableName = "restaurants")
data class RestaurantEntity(
        @PrimaryKey(autoGenerate = true) val id: Long,
        val name: String,
        val city: String,
        val thumbnail: String,
        val type: String,
        val rating: Int,
        val address: String,
        val phone: String,
        @field:SerializedName("special_description") val specialDescription: String,
        val description: String,
        val likes: Int,
        @field:SerializedName("price_rank") val priceRank: Int,
        val url: String
) : Parcelable , ObservableViewModel() {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString())

    override fun writeToParcel(dest: Parcel?, flags: Int) { //To change body of created functions use File | Settings | File Templates.
        dest!!.writeLong(id)
        dest!!.writeString(name)
        dest!!.writeString(city)
        dest!!.writeString(thumbnail)
        dest!!.writeString(type)
        dest!!.writeInt(rating)
        dest!!.writeString(address)
        dest!!.writeString(phone)
        dest!!.writeString(specialDescription)
        dest!!.writeString(description)
        dest!!.writeInt(likes)
        dest!!.writeInt(priceRank)
        dest!!.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "RestaurantEntity(id=$id, name='$name', city='$city', thumbnail='$thumbnail', type='$type', rating=$rating, address='$address', phone='$phone', specialDescription='$specialDescription', description='$description', likes=$likes, priceRank=$priceRank, url='$url')"
    }

    companion object CREATOR : Parcelable.Creator<RestaurantEntity> {
        override fun createFromParcel(parcel: Parcel): RestaurantEntity {
            return RestaurantEntity(parcel)
        }

        override fun newArray(size: Int): Array<RestaurantEntity?> {
            return arrayOfNulls(size)
        }
    }


}
