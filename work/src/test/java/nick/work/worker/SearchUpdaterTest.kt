package nick.work.worker

import io.reactivex.Single
import nick.data.model.Search
import nick.repository.PositionsRepository
import nick.repository.SearchesRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class SearchUpdaterTest {

    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var positionsRepository: PositionsRepository

    @Mock
    lateinit var searchesRepository: SearchesRepository

    @InjectMocks
    lateinit var searchUpdater: SearchUpdater

    val search = Search.EMPTY.copy(isSubscribed = true, lastTimeUserSearched = -1L)

    val searches = listOf(search)

    @Before
    fun setUp() {
        `when`(searchesRepository.queryAllBlocking())
            .thenReturn(searches)
    }

    @Test
    fun findSearchesNeedingUpdating_remotePositionsWereAlreadyCached() {
        `when`(positionsRepository.search(search))
            .thenReturn(Single.fromCallable {
                listOf(
                    createPosition("1"),
                    createPosition("2")
                )
            })
        `when`(positionsRepository.queryCachedPositionsBlocking())
            .thenReturn(listOf(createPosition("1", hasViewed = true),
                createPosition("2", isSaved = true)))

        val toUpdate = searchUpdater.findSearchesNeedingUpdating()

        Assert.assertEquals(0, toUpdate.size)
    }

    @Test
    fun findSearchesNeedingUpdating_remotePositionsWereNeverCached() {
        `when`(positionsRepository.search(search))
            .thenReturn(Single.fromCallable {
                listOf(
                    createPosition("1"),
                    createPosition("2")
                )
            })
        `when`(positionsRepository.queryCachedPositionsBlocking())
            .thenReturn(listOf(createPosition("3", hasViewed = true),
                createPosition("4", isSaved = true)))

        val toUpdate = searchUpdater.findSearchesNeedingUpdating()

        Assert.assertEquals(1, toUpdate.size)
    }
}