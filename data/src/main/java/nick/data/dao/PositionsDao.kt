package nick.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import nick.data.model.Position

/**
 * A Position is considered "cached" if any of it's stateful Boolean columns are true,
 * e.g. is_fresh or is_saved
 */
@Dao
abstract class PositionsDao : BaseDao<Position> {

    companion object {
        const val ORDER_BY_CREATED_AT = "ORDER BY created_at DESC"
    }

    @Query("SELECT * FROM positions WHERE is_fresh = 1 $ORDER_BY_CREATED_AT")
    abstract fun queryFresh(): LiveData<List<Position>>

    @Query("SELECT * FROM positions WHERE is_saved = 1 $ORDER_BY_CREATED_AT")
    abstract fun querySaved(): LiveData<List<Position>>

    @Query("DELETE FROM positions WHERE has_viewed = 0 AND is_saved = 0")
    abstract fun deleteNonCached()

    @Query("SELECT * FROM positions WHERE id = :id")
    abstract fun positionById(id: String): LiveData<Position>

    @Query("SELECT * FROM positions WHERE id IN (:ids)")
    abstract fun positionsByIdsBlocking(ids: List<String>): List<Position>

    @Query("UPDATE positions SET is_fresh = 0 WHERE id NOT IN (:ids)")
    abstract fun updateAllAsNoLongerFreshExcept(ids: List<String>)

    /**
     * If the positionsToInsert are derived from a first-page fetch, then that means all non-cached
     * data should be purged, and all cached data should be set as non-fresh, so that a Position doesn't
     * appear in the fetched result-set (unless that cached Position happens to be found also in that
     * fetched result-set, in which case merge the two to get a newly updated Position).
     */
    @Transaction
    open fun insertWhileReconcilingCachedPositions(
        positionsToInsert: List<Position>,
        isFirstPage: Boolean
    ) {
        if (isFirstPage) {
            deleteNonCached()
        }

        val ids = positionsToInsert.map { it.id }

        if (isFirstPage) {
            // At this point there should only be cached Positions in the database
            updateAllAsNoLongerFreshExcept(ids)
        }

        val reconciledPositionsToInsert = positionsToInsert.toMutableList()
        val matchedPositions = positionsByIdsBlocking(ids)

        matchedPositions.forEach { matchedPosition ->
            positionsToInsert.find { it.id == matchedPosition.id }
                ?.let {
                    reconciledPositionsToInsert.remove(it)
                    reconciledPositionsToInsert.add(
                        // Copy rather than add the matchedPosition because other fields may
                        // be different from what the matchedPosition has.
                        it.copy(
                            isSaved = matchedPosition.isSaved,
                            hasApplied = matchedPosition.hasApplied,
                            hasViewed = matchedPosition.hasViewed
                        )
                    )
                }
        }

        insert(reconciledPositionsToInsert)
    }
}