package nick.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import nick.data.model.Search

@Dao
interface SearchesDao : BaseDao<Search> {

    @Query("SELECT * FROM ${Search.TABLE_NAME}")
    fun queryAll(): LiveData<List<Search>>
}