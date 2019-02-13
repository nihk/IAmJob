package nick.repository

import android.content.SharedPreferences
import nick.core.util.CurrentTime
import nick.data.dao.SearchesDao
import nick.data.model.Search
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class SearchesRepositoryTest {

    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var search: Search

    @Mock
    private lateinit var dao: SearchesDao

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var currentTime: CurrentTime

    @InjectMocks
    private lateinit var searchesRepository: SearchesRepository

    @Before
    fun setUp() {
        Mockito.`when`(currentTime.inMillis()).thenReturn(0L)
    }

    @Test
    fun updateLastTimeUserSearched() {
        searchesRepository.updateLastTimeUserSearched(search).test()

        verify(currentTime).inMillis()

        with(search) {
            Mockito.verify(dao).updateLastTimeUserSearched(
                description, location, isFullTime, 0L
            )
        }
    }
}