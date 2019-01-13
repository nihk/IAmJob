package nick.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Search(
    val description: String,
    val location: Location?,
    val isFullTime: Boolean = false
) : Parcelable