package com.example.peruapptest.util

import io.reactivex.*
import io.reactivex.functions.BiFunction
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @see: https://gist.github.com/tomaszpolanski/99c37c388e06e57ef72a5c8e752b8c2c
 * @see: https://proandroiddev.com/briefly-about-rxjava-logging-20308b013e6d
 */

fun Completable.log(): Completable {
    val tag = tag(COMPLETABLE)
    return doOnEvent { printEvent(tag, it) }
            .doOnSubscribe {
                Log.i(
                    tag,
                    SUBSCRIBE
                )
            }
            .doOnDispose {
                Log.i(
                    tag,
                    DISPOSE
                )
            }
}

inline fun <reified T> Single<T>.log(): Single<T> {
    val tag = tag(SINGLE)
    return doOnEvent { success, error ->
        printEvent(
            tag,
            success,
            error
        )
    }
            .doOnSubscribe {
                Log.i(
                    tag,
                    SUBSCRIBE
                )
            }
            .doOnDispose {
                Log.i(
                    tag,
                    DISPOSE
                )
            }
}

inline fun <reified T> Maybe<T>.log(): Maybe<T> {
    val tag = tag(MAYBE)
    return doOnEvent { success, error ->
        printEvent(
            tag,
            success,
            error
        )
    }
            .doOnSubscribe {
                Log.i(
                    tag,
                    SUBSCRIBE
                )
            }
            .doOnDispose {
                Log.i(
                    tag,
                    DISPOSE
                )
            }
}

inline fun <reified T> Observable<T>.log(): Observable<T> {
    val line = tag(OBSERVABLE)
    return doOnEach {
        Log.i(
            line,
            "$EACH ${it.value.toString()}"
        )
    }
            .doOnSubscribe {
                Log.i(
                    line,
                    SUBSCRIBE
                )
            }
            .doOnDispose {
                Log.i(
                    line,
                    DISPOSE
                )
            }
}

inline fun <reified T> Flowable<T>.log(): Flowable<T> {
    val line = tag(FLOWABLE)
    return doOnEach {
        Log.i(
            line,
            "$EACH ${it.value.toString()}"
        )
    }
            .doOnSubscribe {
                Log.i(
                    line,
                    SUBSCRIBE
                )
            }
            .doOnCancel {
                Log.i(
                    line,
                    CANCEL
                )
            }
}

inline fun <reified T> printEvent(tag: String, success: T?, error: Throwable?) =
        when {
            success == null && error == null ->
                Log.i(
                    tag,
                    COMPLETE
                ) /*With Maybe*/
            success != null ->
                Log.i(
                    tag,
                    "$SUCCESS $success"
                )
            error != null ->
                Log.e(
                    tag,
                    "$ERROR $error"
                )
            else -> Log.e(
                tag,
                UNEXPECTED
            )
        }

fun printEvent(tag: String, error: Throwable?) =
        when {
            error != null -> Log.e(
                tag,
                "$ERROR $error"
            )
            else -> Log.i(tag, COMPLETE)
        }

fun tag(reactiveType: String): String {

    val stackTrace = Thread.currentThread().stackTrace

    val archivosKotlin = stackTrace.filter {
        it.fileName != null && it.fileName.endsWith(".kt")
    }

    if (archivosKotlin.size < 2) return UNTAGGED

    return archivosKotlin[1]
            .let { stack ->
                "$reactiveType{${stack.fileName.removeSuffix(".kt")}::" +
                        stack.methodName + "}"
            }
}


internal fun doOnCompletable(function: () -> Unit): Completable {
    return Completable.create {
        function.invoke()
        it.onComplete()
    }
}

internal fun <T> doOnSingle(function: () -> T): Single<T> {
    return Single.create {
        val value = function.invoke()
        it.onSuccess(value)
    }
}

internal fun <T> Single<T>.interceptar(value: T): Single<T> {
    return onErrorResumeNext { Single.just(value)}
}

internal fun <T> Single<T>.mapOnError(function: () -> T): Single<T> {
    return onErrorResumeNext { doOnSingle { function.invoke() } }
}

internal fun <T> Single<T>.flatMapOnError(single: () -> Single<T>): Single<T> {
    return onErrorResumeNext { single.invoke() }
}

internal fun Completable.doAsync() {
    this.subscribe({},{})
}

internal fun <T> Single<T>.doAsync() {
    this.subscribe({},{})
}

internal fun <K, J> Single<K>.doInParallel(single: Single<J>): Single<Pair<K, J>> {
    return this.zipWith(single, BiFunction { t1: K, t2: J -> Pair(t1, t2) })
}

internal fun <K, J, L, I> Single<K>.doInParallelWithResult(function1: (K) -> Single<J>,
                                                           function2: (K) -> Single<L>,
                                                           transform: (J, L) -> Single<I>):
        Single<I> {
    return flatMap { function1(it).zipWith(function2(it), BiFunction { j: J, l: L -> Pair(j, l) }) }
            .flatMap { transform(it.first, it.second) }

}

internal fun <T> Single<Boolean>.diverge(
                         cumpleCondicion: Observable<T>,
                         noCumpleCondicion: Observable<T>): Observable<T> {
    return this.flatMapObservable { condicion ->
        if (condicion) {
            return@flatMapObservable cumpleCondicion
        } else {
            return@flatMapObservable noCumpleCondicion
        }
    }
}

internal fun <T> Completable.capturarError(observable: Observable<T>): Completable {
    return onErrorResumeNext { Completable.complete() }
            .andThen(observable)
            .flatMapCompletable { Completable.complete() }

}

const val SUBSCRIBE = "onSubscribe()"
const val CANCEL = "onCancel()"
const val EACH = "onEach()"
const val DISPOSE = "onDispose()"
const val ERROR = "Error"
const val SUCCESS = "Success"
const val COMPLETE = "Complete"
const val UNEXPECTED = "Unexpected"

const val OBSERVABLE = "@Observable"
const val COMPLETABLE = "@Completable"
const val FLOWABLE = "@Flowable"
const val SINGLE = "@Single"
const val MAYBE = "@Maybe"

const val UNTAGGED = "Untagged"

object Log {
    val log: Logger = Logger.getLogger("RxLog")
    fun i(tag: String, message: String) = log.log(Level.INFO, "$tag -> $message")
    fun e(tag: String, message: String) = log.log(Level.SEVERE, "$tag -> $message")
}