package com.example.peruapptest.presentation.funciones.home

import com.example.peruapptest.domain.useCase.RoomUseCase
import com.example.peruapptest.presentation.base.BasePresenter
import com.example.peruapptest.presentation.executor.PostExecutionThread
import com.example.peruapptest.presentation.executor.ThreadExecutor
import com.example.peruapptest.util.BaseSingleObserver

class HomePresenter constructor(
    private val roomUseCase: RoomUseCase,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : BasePresenter(threadExecutor, postExecutionThread), HomeMVP.Presenter {

    var mView: HomeMVP.View? = null

    override fun setView(view: HomeMVP.View?) {
        this.mView = view
    }

}