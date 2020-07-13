package com.example.placesdemo.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "result")
data class ResultEntity(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Int = 0,

        @ColumnInfo(name = "place_id")
        val placeId: Int = 0,

        @ColumnInfo(name = "business_status")
        val businessStatus: String? = null,

        @ColumnInfo(name = "name")
        val name: String,

        @ColumnInfo(name = "rating")
        val rating: Double?
)