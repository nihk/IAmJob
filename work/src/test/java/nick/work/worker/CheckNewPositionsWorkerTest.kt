package nick.work.worker

import android.content.Context
import androidx.work.Data
import androidx.work.WorkerParameters
import nick.data.model.Search
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class CheckNewPositionsWorkerTest {

    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var workerParameters: WorkerParameters

    @Mock
    lateinit var searchUpdater: SearchUpdater

    @Mock
    lateinit var newPositionsNotifier: NewPositionsNotifier

    @InjectMocks
    lateinit var worker: CheckNewPositionsWorker

    @Mock
    lateinit var search: Search

    @Before
    fun setUp() {
        `when`(workerParameters.inputData)
            .thenReturn(Data.Builder()
                .putInt(CheckNewPositionsWorker.KEY_SMALL_ICON, 1)
                .putInt(CheckNewPositionsWorker.KEY_NAVIGATION_RES, 2)
                .putInt(CheckNewPositionsWorker.KEY_DESTINATION_ID, 3)
                .build()
            )
    }

    @Test
    fun doWork_doNotNotifyNewResults() {
        `when`(searchUpdater.findSearchesNeedingUpdating())
            .thenReturn(emptyList())

        worker.doWork()

        verify(newPositionsNotifier, times(0))
            .postDeepLinkNotification(anyInt(), anyInt(), anyInt())
    }

    @Test
    fun doWork_notifyNewResults() {
        `when`(searchUpdater.findSearchesNeedingUpdating())
            .thenReturn(listOf(search))

        worker.doWork()

        verify(newPositionsNotifier)
            .postDeepLinkNotification(1, 2, 3)
    }
}