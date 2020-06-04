package com.example.peruapptest.data.repository

import com.example.peruapptest.data.repository.datasources.RoomLocalDataRepository
import com.example.peruapptest.data.room.model.PlaceEntity
import com.example.peruapptest.domain.repository.RoomRepository
import io.reactivex.Completable
import io.reactivex.Single

class RoomDataRepository constructor(
    private val localRepository: RoomLocalDataRepository
) : RoomRepository {

    override fun guardarRoomData(request: PlaceEntity): Single<Int> {
        return localRepository.guardarRoomLocal(request)
    }

    override fun mostrarPlacesGuardadoData(idFirebase: String): Single<List<PlaceEntity>> {
        return localRepository.mostrarPlacesGuardadosLocal(idFirebase)
    }

    override fun eliminarPlaceData(request: PlaceEntity): Single<Int> {
        return localRepository.eliminarPlaceLocal(request)
    }


}