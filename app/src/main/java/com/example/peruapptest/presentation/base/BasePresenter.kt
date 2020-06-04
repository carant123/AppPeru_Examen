package com.example.peruapptest.presentation.base

import androidx.core.util.Preconditions
import com.example.peruapptest.presentation.executor.PostExecutionThread
import com.example.peruapptest.presentation.executor.ThreadExecutor
import com.example.peruapptest.util.log
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

abstract class BasePresenter(
    val threadExecutor: ThreadExecutor,
    val postExecutionThread: PostExecutionThread
) {

    private val disposables: CompositeDisposable = CompositeDisposable()

    inline fun <reified T> subscribir(observable: Observable<T>, observer: DisposableObserver<T>) {

        Preconditions.checkNotNull(observer)
        val disposable = observable
            .log()
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.scheduler)
            .subscribeWith(observer)
        addDisposable(disposable)
    }

    inline fun <reified T> subscribir(single: Single<T>, observer: SingleObserver<T>) {
        Preconditions.checkNotNull(observer)
        single
            .log()
            .subscribeOn(threadExecutor.scheduler)
            .observeOn(postExecutionThread.scheduler)
            .subscribeWith(observer)
    }

    fun subscribir(completable: Completable, observer: CompletableObserver) {
        Preconditions.checkNotNull(observer)
        completable
            .log()
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.scheduler)
            .subscribeWith(observer)
    }

    fun dispose() {
        if (!disposables.isDisposed)
            disposables.dispose()
    }

    fun addDisposable(disposable: Disposable) {
        Preconditions.checkNotNull(disposable)
        Preconditions.checkNotNull(disposables)
        disposables.add(disposable)
    }

}
