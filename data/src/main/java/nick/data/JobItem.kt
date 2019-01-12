package nick.data

import com.squareup.moshi.Json

data class JobItem(
    val id: String,
    val type: String,
    val url: String,
    @Json(name = "created_at")
    val createdAt: String,
    val company: String,
    @Json(name = "company_url")
    val companyUrl: String,
    val location: String,
    val title: String,
    val description: String,
    @Json(name = "how_to_apply")
    val howToApply: String,
    @Json(name = "company_logo")
    val companyLogo: String
)