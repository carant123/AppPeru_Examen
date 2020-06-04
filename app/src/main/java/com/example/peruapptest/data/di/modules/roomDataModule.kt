package com.example.peruapptest.data.di.modules

import android.content.Context
import androidx.room.Room
import com.example.peruapptest.data.repository.RoomDataRepository
import com.example.peruapptest.data.repository.datasources.RoomLocalDataRepository
import com.example.peruapptest.data.room.AppDatabase
import com.example.peruapptest.domain.repository.RoomRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit

var roomDataModule = module {

    factory { RoomLocalDataRepository(get()) }
    factory<RoomRepository> { RoomDataRepository(get()) }
    //single { provideRoom(get()) }
    single { AppDatabase.buildDatabase(get()) }
    single { get<AppDatabase>().mainDao() }

}
