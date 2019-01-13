package nick.search

import nick.data.model.Search
import nick.networking.service.GitHubJobsService
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val service: GitHubJobsService
) {
    fun search(search: Search) = with(search) {
        service.fetchPositions(
            description,
            location.description,
            location.latitude,
            location.longitude,
            isFullTime,
            page
        )
    }
}