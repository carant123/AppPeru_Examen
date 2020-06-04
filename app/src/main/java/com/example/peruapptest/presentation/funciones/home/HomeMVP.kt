package com.example.peruapptest.presentation.funciones.home

import io.reactivex.Completable
import io.reactivex.Single

interface HomeMVP {

    interface View {
        fun iniciarCargar()
        fun finalizarCarga()
        fun mostrarMensajeDeError(mensaje: String)
        fun mostrarMensajeExitoso(mensaje: String)
    }

    interface Presenter {
        fun setView(view: View?)
    }

    interface Model {

    }

}