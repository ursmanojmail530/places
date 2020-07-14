package com.example.placesdemo

import com.example.placesdemo.model.Photo
import com.example.placesdemo.room.database.PlaceDatabase
import com.example.placesdemo.room.entities.PhotoEntity
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.atLeastOnce

class PhotoUnitTest {

    private lateinit var db: PlaceDatabase

    @Before
    fun setup() {
        db = Mockito.mock(PlaceDatabase::class.java)
    }

    @Test
    fun `insertToDatabase SHOULD call insertToDatabase() atleastOnce`() {
        // GIVEN
        val photoEntity = PhotoEntity(0, 0, 100.0, "", 100.0)

        // WHEN
        Photo.insertToDatabase(db, photoEntity)

        // THEN
        Mockito.verify(db, atLeastOnce()).photoDao().insertPhoto(photoEntity)
    }
}