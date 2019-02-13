package nick.work.worker

import nick.data.model.Position
import nick.data.model.Search
import nick.repository.PositionsRepository
import nick.repository.SearchesRepository
import javax.inject.Inject

class SearchUpdater @Inject constructor(
    private val positionsRepository: PositionsRepository,
    private val searchesRepository: SearchesRepository
) {

    fun findSearchesNeedingUpdating(): List<Search> {
        val toUpdate = mutableListOf<Search>()

        val subscribedSearches = searchesRepository.queryAllBlocking()
            .filter(Search::isSubscribed)

        subscribedSearches.forEach { search ->
            val positions: List<Position> = positionsRepository.search(search)
                .onErrorReturnItem(emptyList())
                .blockingGet()

            val numNewResults = positions.filter { position ->
                position.createdAt > search.lastTimeUserSearched
            }.size

            if (numNewResults > 0) {
                toUpdate.add(search.copy(numNewResults = numNewResults))
            }
        }

        return toUpdate
    }

    fun updateSearches(searches: List<Search>) {
        searchesRepository.updateBlocking(searches)
    }
}