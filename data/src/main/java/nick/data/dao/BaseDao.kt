package nick.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntity(entity: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntities(entities: List<T>)

    @Delete
    fun deleteEntity(entity: T)

    @Delete
    fun deleteEntities(entities: List<T>)
}