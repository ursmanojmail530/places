package com.example.placesdemo.room.dao

import androidx.room.*
import com.example.placesdemo.room.entities.PhotoEntity

@Dao
interface PhotoDao {
    @Query("select * from photo")
    fun getAllPhotos(): List<PhotoEntity>

    @Query("select * from photo where result_id in (:id)")
    fun getAllPhotosByPlaceId(id: Int): List<PhotoEntity>

    @Query("select * from photo where id in (:id)")
    fun getPhotoById(id: Int): PhotoEntity

    @Query("delete from photo")
    fun deleteAllPhotos()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: PhotoEntity)

    @Update
    fun updatePhoto(photo: PhotoEntity)

    @Delete
    fun deletePhoto(photo: PhotoEntity)
}