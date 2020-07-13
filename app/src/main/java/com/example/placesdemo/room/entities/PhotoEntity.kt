package com.example.placesdemo.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
data class PhotoEntity(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Int = 0,

        @ColumnInfo(name = "result_id")
        val resultId: Int = 0,

        @ColumnInfo(name = "height")
        val height: Double? = null,

        @ColumnInfo(name = "photo_reference")
        val photoReference: String? = null,

        @ColumnInfo(name = "width")
        val width: Double? = null
)
