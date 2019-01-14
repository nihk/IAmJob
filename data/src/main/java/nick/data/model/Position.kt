package nick.data.model

import com.squareup.moshi.Json

data class Position(
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
) {
}