package com.example.peruapptest.domain.di


import com.example.peruapptest.domain.useCase.RoomUseCase
import com.example.peruapptest.presentation.funciones.home.HomeMVP
import org.koin.dsl.module

val domainModule = module {
    factory { RoomUseCase(get()) }
}