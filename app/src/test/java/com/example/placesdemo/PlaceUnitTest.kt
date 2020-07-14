package com.example.placesdemo

import com.example.placesdemo.model.Place
import com.example.placesdemo.room.database.PlaceDatabase
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.atLeastOnce

class PlaceUnitTest {

    private lateinit var db: PlaceDatabase
    private lateinit var place: Place

    @Before
    fun setup() {
        db = Mockito.mock(PlaceDatabase::class.java)
        place = Place(null)
    }

    @Test
    fun `createOrGetPlace SHOULD call getPlaceByKeyword() atleastOnce`() {
        // GIVEN

        // WHEN
        Place.createOrGetPlace(db, "testString")

        // THEN
        Mockito.verify(db, atLeastOnce()).placeDao().getPlaceByKeyword("testString")
    }

}