package nick.core.util

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun Completable.applySchedulers(): Completable =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.applySchedulers(): Single<T> =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
