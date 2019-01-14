package nick.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import nick.data.model.Position

@Dao
abstract class PositionsDao : BaseDao<Position> {

    companion object {
        const val QUERY_ALL_SAVED = "SELECT * FROM ${Position.TABLE_NAME} WHERE ${Position.COL_IS_SAVED} = 1"
    }

    @Query("SELECT * FROM ${Position.TABLE_NAME} WHERE ${Position.COL_IS_FRESH} = 1")
    abstract fun queryAllFresh(): LiveData<List<Position>>

    @Query(QUERY_ALL_SAVED)
    abstract fun queryAllSaved(): LiveData<List<Position>>

    @Query(QUERY_ALL_SAVED)
    abstract fun queryAllSavedBlocking(): List<Position>

    @Query("DELETE FROM ${Position.TABLE_NAME} WHERE ${Position.COL_IS_SAVED} = 0")
    abstract fun deleteAllUnsaved()

    @Transaction
    open fun deleteAllUnsavedThenInsert(positions: List<Position>) {
        deleteAllUnsaved()
        insert(positions)
    }
}