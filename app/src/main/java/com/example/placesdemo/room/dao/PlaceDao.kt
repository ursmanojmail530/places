package com.example.placesdemo.room.dao

import androidx.room.*
import com.example.placesdemo.room.entities.PlaceEntity

@Dao
interface PlaceDao {
    @Query("select * from place")
    fun getAllPlace(): List<PlaceEntity>

    @Query("select * from place where id in (:id)")
    fun getPlaceById(id: Int): PlaceEntity

    @Query("select * from place where keyword in (:keyword)")
    fun getPlaceByKeyword(keyword: String): PlaceEntity

    @Query("delete from place")
    fun deleteAllPlace()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlace(place: PlaceEntity)

    @Update
    fun updatePlace(place: PlaceEntity)

    @Delete
    fun deletePlace(place: PlaceEntity)
}