package nick.core.util

import android.net.Uri
import android.view.View
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


fun View.visibleOrGone(shouldBeVisible: Boolean) {
    if (shouldBeVisible) {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
    } else {
        if (visibility != View.GONE) {
            visibility = View.GONE
        }
    }
}

fun View.visibleOrInvisible(shouldBeVisible: Boolean) {
    if (shouldBeVisible) {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
    } else {
        if (visibility != View.INVISIBLE) {
            visibility = View.INVISIBLE
        }
    }
}

fun String.lastPathSegment() = requireNotNull(Uri.parse(this).lastPathSegment) {
    "Last path segment for $this was null"
}

// Already in androidx.core-ktx, but not every module is dependent on that
fun String.toUri(): Uri = Uri.parse(this)