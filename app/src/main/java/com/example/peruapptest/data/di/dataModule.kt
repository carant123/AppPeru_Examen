package com.example.peruapptest.data.di


import com.example.peruapptest.data.di.modules.roomDataModule
import com.example.peruapptest.util.listByElementsOf
import org.koin.core.module.Module


val dataModule by lazy {
    listByElementsOf<Module>(
        repositoryModule
    )
}

val repositoryModule by lazy {
    listByElementsOf<Module>(
        roomDataModule
    )
}
