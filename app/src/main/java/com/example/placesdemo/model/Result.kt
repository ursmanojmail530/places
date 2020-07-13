package com.example.placesdemo.model

import com.google.gson.annotations.SerializedName

data class Result(
        @SerializedName("business_status")
        val businessStatus: String? = null,

        @SerializedName("geometry")
        val geometry: Geometry? = null,

        @SerializedName("name")
        val name: String,

        @SerializedName("photos")
        val photos: List<Photo>? = null,

        @SerializedName("rating")
        val rating: Double? = null
)