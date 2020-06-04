package com.example.peruapptest.presentation.funciones.photo

import com.example.peruapptest.data.room.model.PlaceEntity
import com.example.peruapptest.domain.useCase.RoomUseCase
import com.example.peruapptest.presentation.base.BasePresenter
import com.example.peruapptest.presentation.executor.PostExecutionThread
import com.example.peruapptest.presentation.executor.ThreadExecutor
import com.example.peruapptest.presentation.funciones.home.HomeMVP
import com.example.peruapptest.util.BaseSingleObserver

class PhotoPresenter constructor(
    private val roomUseCase: RoomUseCase,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BasePresenter(threadExecutor, postExecutionThread), PhotoMVP.Presenter {

    var mView: PhotoMVP.View? = null

    override fun setView(view: PhotoMVP.View?) {
        this.mView = view
    }

    override fun guardarRoom(request: PlaceEntity) {
        mView?.let {
            it.iniciarCargar()
            subscribir( roomUseCase.guardarModelRoom(request), roomSingleObserver())
        }
    }

    private inner class roomSingleObserver : BaseSingleObserver<Int>() {

        override fun onSuccess(responseModel: Int) {
            mView?.let {
                it.finalizarCarga()
                if (responseModel != 0) {
                    mView?.mostrarMensajeExitoso("result: " + responseModel)
                } else {
                    mView?.mostrarMensajeDeError("result2: " +responseModel)
                }
            }
        }

        override fun onError(exception: Throwable) {
            exception.message?.let { mView?.mostrarMensajeDeError(it) }
        }

    }

}