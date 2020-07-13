package com.example.placesdemo.adapter

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.placesdemo.R
import com.example.placesdemo.model.Result
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.PlacesClient

class PlaceRecyclerViewAdapter(private var placesClient: PlacesClient, private val apiKey: String) : RecyclerView.Adapter<PlaceRecyclerViewAdapter.MyViewHolder>() {

    private val values: MutableList<Result> = ArrayList()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        val ivPic: ImageView = itemView.findViewById(R.id.ivPic)
    }

    fun setData(newValues: List<Result>) {
        values.clear()
        values.addAll(newValues)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.place_recycler_view_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = values[position]
        holder.tvName.text = result.name
        holder.tvRating.text = result.rating.toString()
        if (!result.photos.isNullOrEmpty()) {
            fetchPhoto(holder.ivPic, result.photos[0].photoReference ?: "", result.photos[0].width ?: 100.0, result.photos[0].height ?: 100.0)
        }
    }

    private fun fetchPhoto(iv: ImageView, reference : String,  maxWidth : Double, maxHeight : Double) {
        Log.i("reftest", reference)
        var photoMetadata: PhotoMetadata? = null
        if (!TextUtils.isEmpty(reference)) {
            photoMetadata = PhotoMetadata.builder(reference).build()
        }
        val photoRequestBuilder = FetchPhotoRequest.builder(photoMetadata!!)
            photoRequestBuilder.setMaxWidth(maxWidth.toInt())
            photoRequestBuilder.setMaxHeight(maxHeight.toInt())

        val photoTask = placesClient.fetchPhoto(photoRequestBuilder.build())
        photoTask.addOnSuccessListener { response: FetchPhotoResponse ->
            val bitmap = response.bitmap
            iv.setImageBitmap(bitmap)
        }
        photoTask.addOnFailureListener { exception: Exception ->
            exception.printStackTrace()
        }
        photoTask.addOnCompleteListener { }
    }

}