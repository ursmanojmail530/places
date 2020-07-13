package com.example.placesdemo.room.dao

import androidx.room.*
import com.example.placesdemo.room.entities.LocationEntity

@Dao
interface LocationDao {
    @Query("select * from location")
    fun getAllLocations(): List<LocationEntity>

    @Query("select * from location where result_id in (:id)")
    fun getAllLocationsByResultId(id: Int): List<LocationEntity>

    @Query("select * from location where id in (:id)")
    fun getLocationById(id: Int): LocationEntity

    @Query("delete from location")
    fun deleteAllLocations()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: LocationEntity)

    @Update
    fun updateLocation(location: LocationEntity)

    @Delete
    fun deleteLocation(location: LocationEntity)
}