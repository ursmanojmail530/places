package com.example.placesdemo.model

import com.google.gson.annotations.SerializedName

data class Photo(
        @SerializedName("height")
        val height: Double? = null,
        @SerializedName("photo_reference")
        val photoReference: String? = null,
        @SerializedName("width")
        val width: Double? = null
)
