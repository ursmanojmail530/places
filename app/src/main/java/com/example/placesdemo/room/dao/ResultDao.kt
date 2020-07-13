package com.example.placesdemo.room.dao

import androidx.room.*
import com.example.placesdemo.room.entities.ResultEntity

@Dao
interface ResultDao {
    @Query("select * from result")
    fun getAllResults(): List<ResultEntity>

    @Query("select * from result where place_id in (:id)")
    fun getAllResultsByPlaceId(id: Int): List<ResultEntity>

    @Query("select * from result where id in (:id)")
    fun getResultById(id: Int): ResultEntity

    @Query("select * from result order by id desc limit 1")
    fun getLastRecord(): Int

    @Query("delete from result")
    fun deleteAllResults()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResult(result: ResultEntity)

    @Update
    fun updateResult(result: ResultEntity)

    @Delete
    fun deleteResult(result: ResultEntity)
}