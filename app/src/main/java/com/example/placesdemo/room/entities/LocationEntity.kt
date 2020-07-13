package com.example.placesdemo.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationEntity(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Int = 0,

        @ColumnInfo(name = "result_id")
        val resultId: Int = 0,

        @ColumnInfo(name = "lat")
        val lat: Double? = null,

        @ColumnInfo(name = "lng")
        val lng: Double? = null
)