package com.example.peruapptest.presentation.di


import com.example.peruapptest.data.JobExecutor
import com.example.peruapptest.data.di.dataModule
import com.example.peruapptest.domain.di.domainModule
import com.example.peruapptest.presentation.di.modules.homeModule
import com.example.peruapptest.presentation.di.modules.localPlacesModule
import com.example.peruapptest.presentation.di.modules.photoModule
import com.example.peruapptest.presentation.executor.PostExecutionThread
import com.example.peruapptest.presentation.executor.ThreadExecutor
import com.example.peruapptest.presentation.executor.UIThread
import com.example.peruapptest.util.listByElementsOf
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

val mainModules by lazy {

    listByElementsOf<Module>(
        threadsModule,
        presentationModule,
        domainModule,
        dataModule
    )

}

internal val presentationModule by lazy {

    listByElementsOf<Module>(
        homeModule,
        localPlacesModule,
        photoModule
    )

}

internal val threadsModule = module {
    factory { UIThread() as PostExecutionThread }
    factory { JobExecutor() as ThreadExecutor }
}
