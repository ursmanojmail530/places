package com.example.placesdemo.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "geometry")
data class Geometry(
        @SerializedName("location")
        val location: Location? = null
)