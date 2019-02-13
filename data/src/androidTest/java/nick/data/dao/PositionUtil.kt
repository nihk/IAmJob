package nick.data.dao

import nick.data.model.Position

fun createPosition(
    id: String,
    type: String = "",
    url: String = "",
    createdAt: Long = 0L,
    company: String = "",
    companyUrl: String = "",
    location: String = "",
    title: String = "",
    description: String = "",
    howToApply: String = "",
    companyLogo: String = "",
    isSaved: Boolean = false,
    hasApplied: Boolean = false,
    hasViewed: Boolean = false,
    isFresh: Boolean = true
) = Position(
    id,
    type,
    url,
    createdAt,
    company,
    companyUrl,
    location,
    title,
    description,
    howToApply,
    companyLogo,
    isSaved,
    hasApplied,
    hasViewed,
    isFresh
)

fun createPositions(numPositions: Int): List<Position> {
    val positions = mutableListOf<Position>()

    for (i in 0 until numPositions) {
        positions.add(createPosition(i.toString()))
    }

    return positions
}