package com.hanfeng.pitant.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.hanfeng.pitant.R
import com.hanfeng.pitant.db.RestaurantEntity
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.TextView
import com.hanfeng.pitant.databinding.FragmentRestaurantDetailBinding
import com.hanfeng.pitant.model.Restaurant
import android.content.ClipData
import android.content.ClipboardManager
import android.view.View.OnLongClickListener
import android.databinding.adapters.ViewBindingAdapter.setOnLongClickListener
import android.widget.Toast
import okhttp3.internal.Util
import android.content.Intent
import android.webkit.WebView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RestaurantDetailFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RestaurantDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RestaurantDetailFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        // Obtain ViewModel from ViewModelProviders
        val viewModel = ViewModelProviders.of(this).get(Restaurant::class.java)

        // Obtain binding
        val binding: FragmentRestaurantDetailBinding =
        DataBindingUtil.inflate(
                inflater, R.layout.fragment_restaurant_detail, container, false)

        Log.d("ViewModelActivity", "Restart activity")

        // Bind layout with ViewModel
        binding.restaurant = viewModel

        val view = binding.root

        val restaurant = arguments?.getParcelable<RestaurantEntity>("restaurantEntity")
        Log.d(TAG, "$restaurant")

        view.findViewById<ImageView>(R.id.iv_foods).setImageDrawable(getDrawable(getDrawableName(restaurant!!.thumbnail)))

        view.findViewById<ImageView>(R.id.iv_type).setImageDrawable(getDrawable(getTypeImageName(restaurant!!.type)))

        viewModel.rating.set(restaurant!!.rating)

        viewModel.priceRank.set(restaurant!!.priceRank)

        viewModel.name.set(restaurant!!.name)

        val tvName = view.findViewById<TextView>(R.id.tv_name)
        tvName.setOnLongClickListener {
            val cManager = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val cData = ClipData.newPlainText("text", viewModel.name.get())

            cManager.primaryClip = cData
            Toast.makeText(view.context, "Name Copied",
                    Toast.LENGTH_SHORT).show()
            true
        }

        //view.findViewById<TextView>(R.id.tv_distant).text = "1.8 mi"

        viewModel.likes.set(restaurant!!.likes)

        viewModel.address.set(restaurant!!.address)

        val tvAddress = view.findViewById<TextView>(R.id.tv_address)

        tvAddress.setOnClickListener {
            val intent = Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=${viewModel.address.get()}"))
            startActivity(intent)
        }

        tvAddress.setOnLongClickListener {
            val cManager = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val cData = ClipData.newPlainText("text", viewModel.address.get())

            cManager.primaryClip = cData
            Toast.makeText(view.context, "Address Copied",
                    Toast.LENGTH_SHORT).show()
            true
        }

        val tvPhotoNumber = view.findViewById<TextView>(R.id.tv_phone_number)

        tvPhotoNumber.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", viewModel.phone.get(), null))
            startActivity(intent)
        }

        tvPhotoNumber.setOnLongClickListener {
            val cManager = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val cData = ClipData.newPlainText("text", viewModel.phone.get())

            cManager.primaryClip = cData
            Toast.makeText(view.context, "Phone number Copied",
                    Toast.LENGTH_SHORT).show()
            true
        }


        viewModel.phone.set(restaurant!!.phone)

        viewModel.specialDescription.set(restaurant!!.specialDescription)

        val ivUrl = view.findViewById<ImageView>(R.id.iv_url)

        if (restaurant!!.url != "NONE") {
            ivUrl.visibility = View.VISIBLE

            ivUrl.setOnClickListener {
                val intent = Intent(context, WebViewActivity::class.java)
               // intent.data = Uri.parse(restaurant!!.url)
                intent.putExtra("uri", restaurant!!.url)
                startActivity(intent)
            }
        } else {
           ivUrl.visibility = View.GONE
        }


        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        private val TAG = RestaurantDetailFragment::class.java.simpleName

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RestaurantDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                RestaurantDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun getDrawable(name: String): Drawable {
        Log.d(TAG, "Drawable name: $name")
        val resourceId = this.context!!.resources.getIdentifier(name, "drawable", this.context!!.packageName)
        Log.d(TAG, "Resource ID: $resourceId")

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context!!.resources.getDrawable(resourceId, context!!.theme)
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP")
            context!!.resources.getDrawable(resourceId)
        }
    }

    private fun getDrawableName(drawableTagName : String) : String {
        return drawableTagName.split("/").last()
    }

    private fun getTypeImageName(type : String) : String {
        val s = if (type == "粤") {
            "yue"
        } else {
            "N/A"
        }

        return s
    }
}
