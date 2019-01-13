package nick.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = Position.TABLE_NAME)
data class Position(
    @PrimaryKey
    @ColumnInfo(name = COL_ID)
    val id: String,
    @ColumnInfo(name = COL_TYPE)
    val type: String,
    @ColumnInfo(name = COL_URL)
    val url: String,
    @Json(name = "created_at")
    @ColumnInfo(name = COL_CREATED_AT)
    val createdAt: String?,
    @ColumnInfo(name = COL_COMPANY)
    val company: String,
    @ColumnInfo(name = COL_COMPANY_URL)
    @Json(name = "company_url")
    val companyUrl: String?,
    @ColumnInfo(name = COL_LOCATION)
    val location: String,
    @ColumnInfo(name = COL_TITLE)
    val title: String,
    @ColumnInfo(name = COL_DESCRIPTION)
    val description: String,
    @Json(name = "how_to_apply")
    @ColumnInfo(name = COL_HOW_TO_APPLY)
    val howToApply: String?,
    @Json(name = "company_logo")
    @ColumnInfo(name = COL_COMPANY_LOGO)
    val companyLogo: String?
) : Parcelable {

    companion object {
        const val TABLE_NAME = "positions"
        const val COL_ID = "id"
        const val COL_TYPE = "type"
        const val COL_URL = "url"
        const val COL_CREATED_AT = "created_at"
        const val COL_COMPANY = "company"
        const val COL_COMPANY_URL = "company_url"
        const val COL_LOCATION = "location"
        const val COL_TITLE = "title"
        const val COL_DESCRIPTION = "description"
        const val COL_HOW_TO_APPLY = "how_to_apply"
        const val COL_COMPANY_LOGO = "company_logo"
    }
}