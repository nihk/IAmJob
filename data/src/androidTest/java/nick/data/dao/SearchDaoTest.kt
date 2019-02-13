package nick.data.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import nick.data.JobsDatabase
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SearchDaoTest {

    private lateinit var searchesDao: SearchesDao
    private lateinit var jobsDatabase: JobsDatabase
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        jobsDatabase = Room.inMemoryDatabaseBuilder(
            context, JobsDatabase::class.java
        ).build()
        searchesDao = jobsDatabase.searchesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        jobsDatabase.close()
    }

    @Test
    fun insertOrUpdateLastTimeUserSearched_notInDatabase() {
        val search = createSearch(lastTimeUserSearched = 0L)

        searchesDao.insertOrUpdateLastTimeUserSearched(search, 1L)
        val searches = searchesDao.queryAllBlocking()

        Assert.assertEquals(1, searches.size)
        Assert.assertEquals(0L, searches.first().lastTimeUserSearched)
    }

    @Test
    fun insertOrUpdateLastTimeUserSearched_existsInDatabase() {
        val search = createSearch(lastTimeUserSearched = 0L)
        searchesDao.insert(search)

        searchesDao.insertOrUpdateLastTimeUserSearched(search, 1L)
        val searches = searchesDao.queryAllBlocking()

        Assert.assertEquals(1, searches.size)
        Assert.assertEquals(1L, searches.first().lastTimeUserSearched)
    }
}