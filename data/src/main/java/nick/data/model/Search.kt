package nick.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Search(
    val description: String? = null,
    val location: Location? = null,
    val isFullTime: Boolean = false,
    val page: Int? = null
) : Parcelable {

    companion object {
        val EMPTY = Search()
    }
}