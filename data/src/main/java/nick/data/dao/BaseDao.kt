package nick.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<T>): List<Long>

    @Delete
    fun delete(entity: T): Int

    @Delete
    fun delete(entities: List<T>): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entitiy: T): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entities: List<T>): Int
}