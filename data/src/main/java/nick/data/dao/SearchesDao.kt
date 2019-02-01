package nick.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import nick.data.model.Search

@Dao
interface SearchesDao : BaseDao<Search> {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override fun insert(entity: Search)

    @Query("SELECT * FROM searches")
    fun queryAll(): LiveData<List<Search>>
}