package nick.networking

import android.annotation.SuppressLint
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dagger.Reusable
import nick.data.model.Position
import java.text.SimpleDateFormat
import javax.inject.Inject

@Reusable
class PositionJsonAdapter @Inject constructor() {

    companion object {
        @SuppressLint("SimpleDateFormat")
        val DATE_FORMAT = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
    }

    @JsonClass(generateAdapter = true)
    data class PositionJson(
        val id: String,
        val type: String,
        val url: String,
        @Json(name = "created_at")
        val createdAt: String?,
        val company: String,
        @Json(name = "company_url")
        val companyUrl: String?,
        val location: String,
        val title: String,
        val description: String,
        @Json(name = "how_to_apply")
        val howToApply: String?,
        @Json(name = "company_logo")
        val companyLogo: String?
    )

    @FromJson
    fun fromJson(positionJson: PositionJson) =
        with(positionJson) {
            Position(
                id = id,
                type = type,
                url = url,
                createdAt = DATE_FORMAT.parse(createdAt).time,
                company = company,
                companyUrl = companyUrl,
                location = location,
                title = title,
                description = description,
                howToApply = howToApply,
                companyLogo = companyLogo,
                isSaved = false,
                hasApplied = false,
                hasViewed = false,
                isFresh = true
            )
        }
}