package com.example.placesdemo.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.placesdemo.room.dao.LocationDao
import com.example.placesdemo.room.dao.PhotoDao
import com.example.placesdemo.room.dao.PlaceDao
import com.example.placesdemo.room.dao.ResultDao
import com.example.placesdemo.room.entities.LocationEntity
import com.example.placesdemo.room.entities.PhotoEntity
import com.example.placesdemo.room.entities.PlaceEntity
import com.example.placesdemo.room.entities.ResultEntity

@Database(entities = [LocationEntity::class, PhotoEntity::class, ResultEntity::class, PlaceEntity::class], version = 7, exportSchema = false)
abstract class PlaceDatabase : RoomDatabase() {
    companion object {
        private var INSTANCE: PlaceDatabase? = null
        fun getDataBase(context: Context): PlaceDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, PlaceDatabase::class.java, "places-db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries().build()
            }

            return INSTANCE as PlaceDatabase
        }
    }

    abstract fun locationDao(): LocationDao
    abstract fun photoDao(): PhotoDao
    abstract fun resultDao(): ResultDao
    abstract fun placeDao(): PlaceDao
}