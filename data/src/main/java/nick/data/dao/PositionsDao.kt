package nick.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import nick.data.model.Position

@Dao
interface PositionsDao : BaseDao<Position> {

    @Query("SELECT * FROM ${Position.TABLE_NAME}")
    fun queryAll(): LiveData<List<Position>>

    @Query("DELETE FROM ${Position.TABLE_NAME}")
    fun deleteAll()
}