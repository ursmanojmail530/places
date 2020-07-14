package com.example.placesdemo

import com.example.placesdemo.model.Result
import com.example.placesdemo.room.database.PlaceDatabase
import com.example.placesdemo.room.entities.ResultEntity
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.atLeastOnce

class ResultUnitTest {

    private lateinit var db: PlaceDatabase

    @Before
    fun setup() {
        db = Mockito.mock(PlaceDatabase::class.java)
    }

    @Test
    fun `insertToDatabase SHOULD call insertResult() atleastOnce`() {
        // GIVEN
        val resultEntity = ResultEntity(0, 0, "", "", 1.0)

        // WHEN
        Result.insertToDatabase(db, resultEntity)

        // THEN
        Mockito.verify(db, atLeastOnce()).resultDao().insertResult(resultEntity)
    }
}