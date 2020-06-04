package com.example.peruapptest.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.peruapptest.data.room.model.PlaceEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MainDao {

    @Query("SELECT * FROM Place WHERE idFirebase = :idFirebase ")
    fun getPlaces(idFirebase: String): List<PlaceEntity>

    @Insert
    fun insert(place: PlaceEntity?)

    @Delete
    fun delete(place: PlaceEntity?)

    @Query("DELETE FROM Place")
    fun deleteAllPlaces()

}