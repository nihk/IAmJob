package nick.data.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import nick.data.getValue
import nick.data.JobsDatabase
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PositionsDaoTest {

    private lateinit var positionsDao: PositionsDao
    private lateinit var jobsDatabase: JobsDatabase
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        jobsDatabase = Room.inMemoryDatabaseBuilder(
            context, JobsDatabase::class.java
        ).build()
        positionsDao = jobsDatabase.positionsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        jobsDatabase.close()
    }

    @Test
    fun insertWhileReconcilingCachedPositions_freshViewedPositionBecomesStale() {
        val viewedPosition = createPosition("1", hasViewed = true)
        positionsDao.insert(listOf(viewedPosition))

        val newPosition = createPosition("2")
        positionsDao.insertWhileReconcilingCachedPositions(listOf(newPosition), true)
        val staleViewedPosition = getValue(positionsDao.positionById("1"))!!

        Assert.assertFalse(staleViewedPosition.isFresh)
    }

    @Test
    fun insertWhileReconcilingCachedPositions_freshSavedPositionBecomesStale() {
        val savedPosition = createPosition("1", isSaved = true)
        positionsDao.insert(listOf(savedPosition))

        val newPosition = createPosition("2")
        positionsDao.insertWhileReconcilingCachedPositions(listOf(newPosition), true)
        val staleSavedPosition = getValue(positionsDao.positionById("1"))!!

        Assert.assertFalse(staleSavedPosition.isFresh)
    }

    @Test
    fun insertWhileReconcilingCachedPositions_ephemeralPositionIsDeleted() {
        val ephemeralPosition = createPosition("1")
        positionsDao.insert(listOf(ephemeralPosition))

        val newPosition = createPosition("2")
        positionsDao.insertWhileReconcilingCachedPositions(listOf(newPosition), true)
        val deletedEphemeralPosition = getValue(positionsDao.positionById("1"))

        Assert.assertNull(deletedEphemeralPosition)
    }
}