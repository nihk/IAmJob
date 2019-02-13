package nick.repository

import io.reactivex.Single
import nick.data.dao.PositionsDao
import nick.data.model.Position
import nick.data.model.Search
import nick.networking.GitHubJobsService
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class PositionsRepositoryTest {

    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var service: GitHubJobsService

    @Mock
    private lateinit var positionsDao: PositionsDao

    @InjectMocks
    private lateinit var positionsRepository: PositionsRepository

    @Test
    fun searchThenInsert_hasResults() {
        val positions = listOf(
            Position(
                "", "", "", 0L, "", "", "",
                "", "", "", "", false,
                false, false, true
            )
        )

        with(Search.EMPTY) {
            `when`(positionsRepository.search(this))
                .thenReturn(Single.fromCallable { positions })
            positionsRepository.searchThenInsert(this).test()

            verify(positionsDao).insertWhileReconcilingCachedPositions(positions, isFirstPage())
        }
    }

    @Test
    fun searchThenInsert_doesNotHaveResults() {
        val positions = listOf<Position>()

        with(Search.EMPTY) {
            `when`(positionsRepository.search(this))
                .thenReturn(Single.fromCallable { positions })
            positionsRepository.searchThenInsert(this).test()

            verify(positionsDao, times(0))
                .insertWhileReconcilingCachedPositions(positions, isFirstPage())
        }
    }
}