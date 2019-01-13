package nick.search

import nick.networking.service.GitHubJobsService
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val service: GitHubJobsService
) {
}