package nick.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Location : Parcelable {
    data class LatLng(val lat: Double, val lng: Double) : Location()
    data class Description(val description: String) : Location()
}