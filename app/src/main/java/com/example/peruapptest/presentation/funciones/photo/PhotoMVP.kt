package com.example.peruapptest.presentation.funciones.photo

import com.example.peruapptest.data.room.model.PlaceEntity

interface PhotoMVP {

    interface View {
        fun iniciarCargar()
        fun finalizarCarga()
        fun mostrarMensajeDeError(mensaje: String)
        fun mostrarMensajeExitoso(mensaje: String)
    }

    interface Presenter {
        fun setView(view: View?)
        fun guardarRoom(request: PlaceEntity)
    }

    interface Model {

    }

}