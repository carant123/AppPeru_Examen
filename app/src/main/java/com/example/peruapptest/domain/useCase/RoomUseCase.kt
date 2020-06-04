package com.example.peruapptest.domain.useCase

import com.example.peruapptest.data.room.model.PlaceEntity
import com.example.peruapptest.domain.repository.RoomRepository
import io.reactivex.Single

class RoomUseCase constructor( private val repository: RoomRepository ) {

    fun guardarModelRoom(request: PlaceEntity): Single<Int> {
        return repository.guardarRoomData(request)
    }

    fun mostrarPlacesGuardadoDomain(idFirebase: String) : Single<List<PlaceEntity>> {
        return repository.mostrarPlacesGuardadoData(idFirebase)
    }

    fun eliminarPlaceLocalDomain(request: PlaceEntity): Single<Int> {
        return repository.eliminarPlaceData(request)
    }

}