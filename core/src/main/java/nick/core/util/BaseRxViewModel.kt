package nick.core.util

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseRxViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    protected fun addDisposable(d: Disposable) {
        compositeDisposable.add(d)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}