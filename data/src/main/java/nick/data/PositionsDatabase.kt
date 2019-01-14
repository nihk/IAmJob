package nick.data

import androidx.room.Database
import androidx.room.RoomDatabase
import nick.data.dao.PositionsDao
import nick.data.model.Position

@Database(
    entities = [Position::class],
    version = 1,
    exportSchema = false
)
abstract class PositionsDatabase : RoomDatabase() {
    
    abstract fun positionsDao(): PositionsDao

    companion object {
        const val DATABASE_NAME = "positions.db"
    }
}