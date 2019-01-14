package nick.networking

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import nick.core.di.ApplicationScope
import nick.data.model.Position
import javax.inject.Inject

@ApplicationScope
class PositionJsonAdapter @Inject constructor() {

    data class PositionJson(
        val id: String,
        val type: String,
        val url: String,
        @Json(name = "created_at") val createdAt: String?,
        val company: String,
        @Json(name = "company_url") val companyUrl: String?,
        val location: String,
        val title: String,
        val description: String,
        val howToApply: String?,
        val companyLogo: String?
    )

    @FromJson
    fun fromJson(positionJson: PositionJson) =
        with(positionJson) {
            Position(
                id = id,
                type = type,
                url = url,
                createdAt = createdAt,
                company = company,
                companyUrl = companyUrl,
                location = location,
                title = title,
                description = description,
                howToApply = howToApply,
                companyLogo = companyLogo,
                isSaved = false,
                hasApplied = false,
                isFresh = true
            )
        }
}