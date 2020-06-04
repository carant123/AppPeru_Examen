package com.example.peruapptest

import android.app.Application
import com.example.peruapptest.data.room.AppDatabase
import com.example.peruapptest.presentation.di.mainModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        //initializeDatabaseRoom()
        injectarModulos()
    }

//    private fun initializeDatabaseRoom() {
//        AppDatabase.getDatabase(this)
//    }

    private fun injectarModulos() {
        val context = this
        startKoin {
            androidContext(context)
            modules(mainModules) }
    }

}