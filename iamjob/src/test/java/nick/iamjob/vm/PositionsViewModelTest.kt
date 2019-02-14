package nick.iamjob.vm

import io.reactivex.Completable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import nick.data.model.Position
import nick.data.model.Search
import nick.repository.PositionsRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


class PositionsViewModelTest {

    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var repository: PositionsRepository

    @InjectMocks
    lateinit var viewModel: PositionsViewModel

    val search = Search.EMPTY

    val defaultPosition = Position(
        "", "", "", 0L, "", "",
        "", "", "", "", "", isSaved = false,
        hasApplied = false, hasViewed = false, isFresh = false
    )

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun setPositionViewed_isAlreadyViewed() {
        val position = defaultPosition.copy(hasViewed = true)

        viewModel.setPositionViewed(position)

        verify(repository, times(0))
            .updatePosition(position)
    }

    @Test
    fun setPositionViewed_firstTimeViewed() {
        val position = defaultPosition.copy(hasViewed = false)
        val hasViewedPosition = position.copy(hasViewed = true)
        `when`(repository.updatePosition(hasViewedPosition))
            .thenReturn(Completable.complete())

        viewModel.setPositionViewed(position)

        verify(repository).updatePosition(hasViewedPosition)
    }

    @Test
    fun maybePaginate_dont() {
        val visibleItemCount = 1
        val lastVisibleItemCount = 1
        val totalItemCount = 100
        val itemsFromEndThreshold = 1

        viewModel.maybePaginate(
            search,
            visibleItemCount,
            lastVisibleItemCount,
            totalItemCount,
            itemsFromEndThreshold
        )

        verify(repository, times(0))
            .searchThenInsert(search)
    }

    @Test
    fun maybePaginate_do() {
        val visibleItemCount = 1
        val lastVisibleItemCount = 1
        val totalItemCount = 100
        val itemsFromEndThreshold = 1
        `when`(repository.searchThenInsert(search))
            .thenReturn(Completable.complete())

        viewModel.maybePaginate(
            search,
            visibleItemCount,
            lastVisibleItemCount,
            totalItemCount,
            itemsFromEndThreshold
        )

        verify(repository, times(0))
            .searchThenInsert(search)
    }
}