package nick.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import nick.data.model.PositionColumnNames
import nick.data.model.SavedPosition

@Dao
interface SavedPositionsDao : BaseDao<SavedPosition> {

    @Query("SELECT * FROM ${SavedPosition.TABLE_NAME}")
    fun queryAll(): LiveData<List<SavedPosition>>

    @Query("SELECT * FROM ${SavedPosition.TABLE_NAME}")
    fun queryAllBlocking(): List<SavedPosition>

    @Query("DELETE FROM ${SavedPosition.TABLE_NAME} WHERE ${PositionColumnNames.COL_ID} = :id")
    fun deleteById(id: String)
}