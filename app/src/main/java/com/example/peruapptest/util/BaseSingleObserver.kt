package com.example.peruapptest.util

import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

open class BaseSingleObserver<T> : SingleObserver<T> {

    override fun onSuccess(t: T) = Unit

    override fun onSubscribe(d: Disposable) = Unit

    override fun onError(exception: Throwable) = Unit
}
