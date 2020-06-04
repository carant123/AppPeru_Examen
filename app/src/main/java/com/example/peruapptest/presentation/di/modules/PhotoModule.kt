package com.example.peruapptest.presentation.di.modules

import com.example.peruapptest.presentation.funciones.photo.PhotoMVP
import com.example.peruapptest.presentation.funciones.photo.PhotoPresenter
import org.koin.dsl.module

var photoModule = module {

    factory<PhotoMVP.Presenter> {
        PhotoPresenter(get(), get(), get())
    }

}