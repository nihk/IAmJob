package nick.ui

import android.view.View

fun View.visibleOrGone(shouldBeVisible: Boolean) {
    setVisibility(this, shouldBeVisible, View.GONE)
}

fun View.visibleOrInvisible(shouldBeVisible: Boolean) {
    setVisibility(this, shouldBeVisible, View.INVISIBLE)
}

private fun setVisibility(view: View, shouldBeVisible: Boolean, notVisibleState: Int) {
    with(view) {
        if (shouldBeVisible) {
            if (visibility != View.VISIBLE) {
                visibility = View.VISIBLE
            }
        } else {
            if (visibility != notVisibleState) {
                visibility = notVisibleState
            }
        }
    }
}