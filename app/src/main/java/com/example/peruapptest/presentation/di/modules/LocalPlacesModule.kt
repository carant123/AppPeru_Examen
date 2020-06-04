package com.example.peruapptest.presentation.di.modules

import com.example.peruapptest.presentation.funciones.localPlaces.LocalPlacesMVP
import com.example.peruapptest.presentation.funciones.localPlaces.LocalPlacesPresenter
import org.koin.dsl.module

var localPlacesModule = module {

    factory<LocalPlacesMVP.Presenter> {
        LocalPlacesPresenter(get(), get(), get())
    }

}