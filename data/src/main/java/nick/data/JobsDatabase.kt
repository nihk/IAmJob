package nick.data

import androidx.room.Database
import androidx.room.RoomDatabase
import nick.data.dao.PositionsDao
import nick.data.dao.SearchesDao
import nick.data.model.Position
import nick.data.model.Search

@Database(
    entities = [Position::class, Search::class],
    version = 1,
    exportSchema = false
)
abstract class JobsDatabase : RoomDatabase() {
    
    abstract fun positionsDao(): PositionsDao
    abstract fun searchesDao(): SearchesDao

    companion object {
        const val DATABASE_NAME = "jobs.db"
    }
}