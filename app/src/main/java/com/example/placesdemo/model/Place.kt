package com.example.placesdemo.model

import com.google.gson.annotations.SerializedName

data class Place(
        @SerializedName("results")
        val results: List<Result>? = null
)