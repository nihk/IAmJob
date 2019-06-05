package nick.core.util

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result = MediatorLiveData<Resource<ResultType>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        result.value = Resource.Loading()
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.Success(newData))
                }
            }
        }
    }

    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        createCall()
            .flatMapCompletable { items -> saveCallResult(items) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    result.addSource(dbSource) { newData ->
                        compositeDisposable.add(d)
                        setValue(Resource.Loading(newData))
                    }
                }

                override fun onComplete() {
                    result.removeSource(dbSource)
                    result.addSource(loadFromDb()) {
                        result.value = Resource.Success(it)
                    }
                }

                override fun onError(e: Throwable) {
                    onFetchFailed()
                    result.removeSource(dbSource)
                    result.addSource(dbSource) {
                        result.value = Resource.Failure(e, it)
                    }
                }
            })
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    fun cancel() {
        compositeDisposable.clear()
    }

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected open fun shouldFetch(data: ResultType?): Boolean = true

    @MainThread
    protected abstract fun createCall(): Single<RequestType>

    protected open fun onFetchFailed() {}

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType): Completable
}