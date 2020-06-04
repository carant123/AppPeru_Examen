package com.example.peruapptest.domain.repository

import com.example.peruapptest.data.room.model.PlaceEntity
import io.reactivex.Completable
import io.reactivex.Single

interface RoomRepository {
    fun guardarRoomData(request: PlaceEntity) : Single<Int>
    fun mostrarPlacesGuardadoData(idFirebase: String) : Single<List<PlaceEntity>>
    fun eliminarPlaceData(request: PlaceEntity) : Single<Int>
}