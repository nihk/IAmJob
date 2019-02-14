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

        val cachedPositions = positionsRepository.queryCachedPositionsBlocking()

        subscribedSearches.forEach { search ->
            val fetchedPositions: List<Position> = positionsRepository.search(search)
                .onErrorReturnItem(emptyList())
                .blockingGet()

            val numNewResults = fetchedPositions.filter { fetchedPosition ->
                fetchedPosition.createdAt > search.lastTimeUserSearched
                        && !isCachedPosition(fetchedPosition, cachedPositions)
            }.size

            if (numNewResults > 0) {
                toUpdate.add(search.copy(numNewResults = numNewResults))
            }
        }

        return toUpdate
    }

    private fun isCachedPosition(position: Position, cachedPositions: List<Position>) =
        cachedPositions.find { it.id == position.id } != null

    fun updateSearches(searches: List<Search>) {
        searchesRepository.updateBlocking(searches)
    }
}