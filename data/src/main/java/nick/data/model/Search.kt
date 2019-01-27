package nick.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = Search.TABLE_NAME,
    primaryKeys = [
        Search.COL_DESCRIPTION, Search.COL_LOCATION, Search.COL_IS_FULL_TIME
    ]
)
data class Search @JvmOverloads constructor(
    @ColumnInfo(name = COL_DESCRIPTION) val description: String,
    @ColumnInfo(name = COL_LOCATION) val location: String,
    @ColumnInfo(name = COL_IS_FULL_TIME) val isFullTime: Boolean,
    @Ignore val page: Int = 1
) : Parcelable {

    companion object {
        val EMPTY = Search(
            description = "",
            location = "",
            isFullTime = false
        )

        const val TABLE_NAME = "searches"
        const val COL_DESCRIPTION = "description"
        const val COL_LOCATION = "location"
        const val COL_IS_FULL_TIME = "is_full_time"
    }

    // Ignores page
    fun isEmpty(): Boolean {
        return this.description == EMPTY.description
                && this.location == EMPTY.location
                && this.isFullTime == EMPTY.isFullTime
    }

    fun arePagesExhausted() = page == -1
    fun toFirstPage() = copy(page = 1)
    fun toExhausted() = copy(page = -1)
}