package nick.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    val description: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
) : Parcelable {

    init {
        // Workaround for my laziness -- the GitHub Jobs API wants either a location description
        // or a Lat/Lng, i.e. not both
        val allNull = description == null && latitude == null && longitude == null
        val badCombo = description != null && (latitude != null || longitude != null)
        val incompletePair = description == null && (latitude == null || longitude == null)

        if (allNull || badCombo || incompletePair) {
            error("Location wasn't set up correctly")
        }
    }
}