package nick.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import nick.data.model.Position
import nick.data.model.SavedPosition

@Dao
interface SavedPositionsDao : BaseDao<SavedPosition> {

    @Query("SELECT * FROM ${SavedPosition.TABLE_NAME}")
    fun queryAll(): LiveData<List<SavedPosition>>

    @Query("SELECT * FROM ${SavedPosition.TABLE_NAME} WHERE ${SavedPosition.COL_POSITION_PREFIX + Position.COL_ID} = :positionId")
    fun querySavedPosition(positionId: String): LiveData<SavedPosition>
}