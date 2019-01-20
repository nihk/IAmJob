package nick.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = Position.TABLE_NAME)
data class Position(
    @PrimaryKey @ColumnInfo(name = COL_ID) val id: String,
    @ColumnInfo(name = COL_TYPE) val type: String,
    @ColumnInfo(name = COL_URL) val url: String,
    @ColumnInfo(name = COL_CREATED_AT) val createdAt: Long,
    @ColumnInfo(name = COL_COMPANY) val company: String,
    @ColumnInfo(name = COL_COMPANY_URL) val companyUrl: String?,
    @ColumnInfo(name = COL_LOCATION) val location: String,
    @ColumnInfo(name = COL_TITLE) val title: String,
    @ColumnInfo(name = COL_DESCRIPTION) val description: String,
    @ColumnInfo(name = COL_HOW_TO_APPLY) val howToApply: String?,
    @ColumnInfo(name = COL_COMPANY_LOGO) val companyLogo: String?,
    @ColumnInfo(name = COL_IS_SAVED) val isSaved: Boolean,
    @ColumnInfo(name = COL_HAS_APPLIED) val hasApplied: Boolean,
    @ColumnInfo(name = COL_HAS_VIEWED) val hasViewed: Boolean,
    /**
     * Saved Positions (i.e. Position.isSaved == true) will persist locally in the same table with
     * non-saved Positions, which are deleted from the table on each new search for more Positions.
     * A saved Position should show up in a list if it has the same Position.id
     * as one of the incoming Positions that were fetched remotely. It should otherwise not
     * show up in that list. This fields serves as a flag to distinguish whether it should
     * be shown in a list of that "fresh" data.
     */
    @ColumnInfo(name = COL_IS_FRESH) val isFresh: Boolean
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
        const val COL_IS_SAVED = "is_saved"
        const val COL_HAS_APPLIED = "has_applied"
        const val COL_HAS_VIEWED = "has_viewed"
        const val COL_IS_FRESH = "is_fresh"
    }
}