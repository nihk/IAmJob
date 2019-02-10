package nick.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import nick.data.model.Search

const val QUERY_ALL_SEARCHES = "SELECT * FROM searches"

@Dao
abstract class SearchesDao : BaseDao<Search> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract override fun insert(entity: Search): Long

    @Transaction
    open fun insertOrUpdateLastTimeUserSearched(
        search: Search,
        currentTimeInMillis: Long
    ) {
        val id = insert(search)
        if (id == -1L) {
            with(search) {
                updateLastTimeUserSearched(description, location, isFullTime, currentTimeInMillis)
            }
        }
    }

    @Query(QUERY_ALL_SEARCHES)
    abstract fun queryAll(): LiveData<List<Search>>

    @Query(QUERY_ALL_SEARCHES)
    abstract fun queryAllBlocking(): List<Search>

    @Query("UPDATE searches SET last_time_user_searched = :currentTimeInMillis, num_new_results = 0 WHERE description = :description AND location = :location AND is_full_time = :isFullTime")
    abstract fun updateLastTimeUserSearched(
        description: String,
        location: String,
        isFullTime: Boolean,
        currentTimeInMillis: Long
    )
}