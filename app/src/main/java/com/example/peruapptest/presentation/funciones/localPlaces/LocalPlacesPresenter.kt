package com.example.peruapptest.presentation.funciones.localPlaces

import com.example.peruapptest.data.room.model.PlaceEntity
import com.example.peruapptest.domain.useCase.RoomUseCase
import com.example.peruapptest.presentation.base.BasePresenter
import com.example.peruapptest.presentation.executor.PostExecutionThread
import com.example.peruapptest.presentation.executor.ThreadExecutor
import com.example.peruapptest.util.BaseSingleObserver


class LocalPlacesPresenter constructor(
    private val roomUseCase: RoomUseCase,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BasePresenter(threadExecutor, postExecutionThread), LocalPlacesMVP.Presenter {

    var mView: LocalPlacesMVP.View? = null

    override fun setView(view: LocalPlacesMVP.View?) {
        this.mView = view
    }

    override fun obtenerListaPlacesLocales(idFirebase: String) {
        mView?.let {
            it.iniciarCargar()
            subscribir( roomUseCase.mostrarPlacesGuardadoDomain(idFirebase), roomSingleObserver())
        }
    }

    override fun eliminarPlaceLocal(entity: PlaceEntity) {
        mView?.let {
            it.iniciarCargar()
            subscribir( roomUseCase.eliminarPlaceLocalDomain(entity), roomEliminarSingleObserver())
        }
    }

    private inner class roomSingleObserver : BaseSingleObserver<List<PlaceEntity>>() {

        override fun onSuccess(lista: List<PlaceEntity>) {
            mView?.let {
                it.finalizarCarga()
                it?.mostrarPlacesListaLocal(lista)
                if (lista.isEmpty()) {
                    it?.mostrarMensajeDeError("Vacio")
                }
            }
        }

        override fun onError(exception: Throwable) {
            exception.message?.let { mView?.mostrarMensajeDeError(it) }
        }

    }

    private inner class roomEliminarSingleObserver : BaseSingleObserver<Int>() {

        override fun onSuccess(codigo: Int) {
            mView?.let {
                it.finalizarCarga()
                if (codigo == 1) {
                    mView?.mostrarMensajePlaceLocalEliminar()
                }
            }
        }

        override fun onError(exception: Throwable) {
            exception.message?.let { mView?.mostrarMensajeDeError(it) }
        }

    }


}