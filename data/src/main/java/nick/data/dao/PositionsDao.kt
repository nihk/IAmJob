package nick.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import nick.data.model.Position

@Dao
abstract class PositionsDao : BaseDao<Position> {

    companion object {
        const val ORDER_BY_CREATED_AT = "ORDER BY ${Position.COL_CREATED_AT} DESC"
    }

    @Query("SELECT * FROM ${Position.TABLE_NAME} WHERE ${Position.COL_IS_FRESH} = 1 $ORDER_BY_CREATED_AT")
    abstract fun queryFresh(): LiveData<List<Position>>

    @Query("SELECT * FROM ${Position.TABLE_NAME} WHERE ${Position.COL_IS_SAVED} = 1 AND ${Position.COL_IS_FRESH} = 1 $ORDER_BY_CREATED_AT")
    abstract fun querySavedFresh(): LiveData<List<Position>>

    @Query("SELECT * FROM ${Position.TABLE_NAME} WHERE (${Position.COL_HAS_VIEWED} = 1 OR ${Position.COL_IS_SAVED} = 1) AND ${Position.COL_IS_FRESH} = 1")
    abstract fun queryCachedFreshBlocking(): List<Position>

    @Query("DELETE FROM ${Position.TABLE_NAME} WHERE ${Position.COL_HAS_VIEWED} = 0 AND ${Position.COL_IS_SAVED} = 0")
    abstract fun deleteNonCachable()

    @Transaction
    open fun deleteAllNonCachableThenInsert(positions: List<Position>) {
        deleteNonCachable()
        insert(positions)
    }
}