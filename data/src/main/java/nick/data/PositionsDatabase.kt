package nick.data

import androidx.room.Database
import androidx.room.RoomDatabase
import nick.data.dao.PositionsDao
import nick.data.dao.SavedPositionsDao
import nick.data.model.Position
import nick.data.model.SavedPosition

@Database(
    entities = [Position::class, SavedPosition::class],
    version = 1,
    exportSchema = false
)
abstract class PositionsDatabase : RoomDatabase() {

    abstract fun positionsDao(): PositionsDao
    abstract fun savedPositionsDao(): SavedPositionsDao

    companion object {
        const val DATABASE_NAME = "positions.db"
    }
}