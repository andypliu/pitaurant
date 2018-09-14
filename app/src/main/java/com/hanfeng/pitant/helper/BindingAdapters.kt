package com.hanfeng.pitant.helper

import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.hanfeng.pitant.R

object BindingAdapters {
    /**
     * A Binding Adapter that is called whenever the value of the attribute `app:ratingIcon`
     * changes. Receives a popularity level that determines the icon and tint color to use.
     */
    @BindingAdapter("app:ratingIcon")
    @JvmStatic
    fun ratingIcon(view: ImageView, rating: Int) {
        view.setImageDrawable(getRatingDrawable(rating, view.context))
    }

    fun getRatingDrawable(rating: Int, context: Context): Drawable? {
        return when (rating) {
            1 -> {
                ContextCompat.getDrawable(context, R.drawable.rating_1)
            }
            2 -> {
                ContextCompat.getDrawable(context, R.drawable.rating_2)
            }
            3-> {
                ContextCompat.getDrawable(context, R.drawable.rating_3)
            }
            4-> {
                ContextCompat.getDrawable(context, R.drawable.rating_4)
            }
            5-> {
                ContextCompat.getDrawable(context, R.drawable.rating_5)
            }
            else -> {
                ContextCompat.getDrawable(context, R.drawable.rating_1)
            }
        }
    }

    /**
     * A Binding Adapter that is called whenever the value of the attribute `app:pricingIcon`
     * changes. Receives a popularity level that determines the icon and tint color to use.
     */
    @BindingAdapter("app:pricingIcon")
    @JvmStatic
    fun pricingIcon(view: ImageView, pricing: Int) {
        view.setImageDrawable(getPricingDrawable(pricing, view.context))
    }

    private fun getPricingDrawable(pricing: Int, context: Context): Drawable? {
        return when (pricing) {
            1 -> {
                ContextCompat.getDrawable(context, R.drawable.price_rank_1)
            }
            2 -> {
                ContextCompat.getDrawable(context, R.drawable.price_rank_2)
            }
            3-> {
                ContextCompat.getDrawable(context, R.drawable.price_rank_3)
            }
            4-> {
                ContextCompat.getDrawable(context, R.drawable.price_rank_4)
            }
            5-> {
                ContextCompat.getDrawable(context, R.drawable.price_rank_5)
            }
            else -> {
                ContextCompat.getDrawable(context, R.drawable.price_rank_1)
            }
        }
    }
}