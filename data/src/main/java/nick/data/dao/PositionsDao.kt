package nick.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import nick.data.model.Position

@Dao
abstract class PositionsDao : BaseDao<Position> {

    companion object {
        const val ORDER_BY_CREATED_AT = "ORDER BY created_at DESC"
    }

    @Query("SELECT * FROM positions WHERE is_fresh = 1 $ORDER_BY_CREATED_AT")
    abstract fun queryFresh(): LiveData<List<Position>>

    @Query("SELECT * FROM positions WHERE is_saved = 1 $ORDER_BY_CREATED_AT")
    abstract fun querySaved(): LiveData<List<Position>>

    @Query("SELECT * FROM positions WHERE (has_viewed = 1 OR is_saved = 1)")
    abstract fun queryCachedBlocking(): List<Position>

    @Query("DELETE FROM positions WHERE has_viewed = 0 AND is_saved = 0")
    abstract fun deleteNonCachable()

    @Query("SELECT * FROM positions WHERE id = :id")
    abstract fun positionById(id: String): LiveData<Position>

    @Transaction
    open fun deleteAllNonCachableThenInsert(positions: List<Position>) {
        deleteNonCachable()
        insert(positions)
    }
}