package nick.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import nick.data.model.Position

@Dao
abstract class PositionsDao : BaseDao<Position> {

    companion object {
        const val QUERY_SAVED = "SELECT * FROM ${Position.TABLE_NAME} WHERE ${Position.COL_IS_SAVED} = 1"
    }

    @Query("SELECT * FROM ${Position.TABLE_NAME} WHERE ${Position.COL_IS_FRESH} = 1")
    abstract fun queryFresh(): LiveData<List<Position>>

    @Query(QUERY_SAVED)
    abstract fun querySaved(): LiveData<List<Position>>

    @Query(QUERY_SAVED)
    abstract fun querySavedBlocking(): List<Position>

    @Query("DELETE FROM ${Position.TABLE_NAME} WHERE ${Position.COL_IS_SAVED} = 0")
    abstract fun deleteUnsaved()

    @Transaction
    open fun deleteAllUnsavedThenInsert(positions: List<Position>) {
        deleteUnsaved()
        insert(positions)
    }
}