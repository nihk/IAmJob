package nick.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import nick.data.model.SavedPosition

@Dao
interface SavedPositionsDao : BaseDao<SavedPosition> {

    @Query("SELECT * FROM ${SavedPosition.TABLE_NAME}")
    fun queryAll(): LiveData<List<SavedPosition>>

    @Delete
    fun delete(savedPosition: SavedPosition)
}