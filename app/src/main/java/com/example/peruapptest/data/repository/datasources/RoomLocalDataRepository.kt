package com.example.peruapptest.data.repository.datasources

import android.util.Log
import com.example.peruapptest.data.room.AppDatabase
import com.example.peruapptest.data.room.MainDao
import com.example.peruapptest.data.room.model.PlaceEntity
import io.reactivex.Completable
import io.reactivex.Single

class RoomLocalDataRepository(private val dbRoom: MainDao) {

    fun guardarRoomLocal(request: PlaceEntity) : Single<Int> {
        return Single.fromCallable { guardarPlaceSingle (request) }
    }

    fun mostrarPlacesGuardadosLocal(idFirebase: String) : Single<List<PlaceEntity>> {
        return Single.fromCallable { dbRoom?.getPlaces(idFirebase) }
    }

    fun eliminarPlaceLocal(request: PlaceEntity) : Single<Int> {
        return Single.fromCallable { eliminarPlaceSingle(request) }
    }

    private fun eliminarPlaceSingle(place: PlaceEntity) : Int {
        dbRoom?.delete(place)
        return 1
    }

    private fun guardarPlaceSingle(place: PlaceEntity) : Int {
        dbRoom?.insert(place)
        return 1
    }

}