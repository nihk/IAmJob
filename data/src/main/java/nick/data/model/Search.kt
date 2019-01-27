package nick.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Search(
    val description: String = "",
    val location: String = "",
    val isFullTime: Boolean = false,
    val page: Int = 0
) : Parcelable {

    companion object {
        val EMPTY = Search()
    }
}