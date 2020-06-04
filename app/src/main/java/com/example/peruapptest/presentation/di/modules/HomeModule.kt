package com.example.peruapptest.presentation.di.modules

import com.example.peruapptest.presentation.funciones.home.HomeMVP
import com.example.peruapptest.presentation.funciones.home.HomePresenter
import org.koin.dsl.module

var homeModule = module {

    factory<HomeMVP.Presenter> {
        HomePresenter(get(), get(), get())
    }

}