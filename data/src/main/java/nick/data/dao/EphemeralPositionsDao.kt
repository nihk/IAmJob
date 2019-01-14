package nick.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import nick.data.model.EphemeralPosition

@Dao
interface EphemeralPositionsDao : BaseDao<EphemeralPosition> {

    @Query("SELECT * FROM ${EphemeralPosition.TABLE_NAME}")
    fun queryAll(): LiveData<List<EphemeralPosition>>

    @Query("DELETE FROM ${EphemeralPosition.TABLE_NAME}")
    fun deleteAll()
}