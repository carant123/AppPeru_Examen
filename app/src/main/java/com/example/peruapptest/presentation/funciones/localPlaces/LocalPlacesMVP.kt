package com.example.peruapptest.presentation.funciones.localPlaces

import com.example.peruapptest.data.room.model.PlaceEntity

interface LocalPlacesMVP {

    interface View {
        fun iniciarCargar()
        fun finalizarCarga()
        fun mostrarPlacesListaLocal(lista: List<PlaceEntity>)
        fun mostrarMensajeDeError(mensaje: String)
        fun mostrarMensajeExitoso(mensaje: String)
        fun mostrarMensajePlaceLocalEliminar()
    }

    interface Presenter {
        fun setView(view: View?)
        fun obtenerListaPlacesLocales(idFirebase: String)
        fun eliminarPlaceLocal(entity: PlaceEntity)
    }

    interface Model {

    }

}