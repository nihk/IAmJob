package nick.data.dao

import nick.data.model.Search

fun createSearch(
    description: String = "",
    location: String = "",
    isFullTime: Boolean = false,
    isSubscribed: Boolean = false,
    numNewResults: Int = 0,
    lastTimeUserSearched: Long = 0L,
    page: Int = 1
) = Search(
    description,
    location,
    isFullTime,
    isSubscribed,
    numNewResults,
    lastTimeUserSearched,
    page
)