package com.example.placesdemo.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place")
data class PlaceEntity(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Int = 0,

        @ColumnInfo(name = "keyword")
        val keyword: String? = null
)