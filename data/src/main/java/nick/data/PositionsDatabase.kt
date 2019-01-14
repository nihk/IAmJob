package nick.data

import androidx.room.Database
import androidx.room.RoomDatabase
import nick.data.dao.EphemeralPositionsDao
import nick.data.dao.SavedPositionsDao
import nick.data.model.EphemeralPosition
import nick.data.model.SavedPosition

@Database(
    entities = [EphemeralPosition::class, SavedPosition::class],
    version = 1,
    exportSchema = false
)
abstract class PositionsDatabase : RoomDatabase() {

    abstract fun ephemeralPositionsDao(): EphemeralPositionsDao
    abstract fun savedPositionsDao(): SavedPositionsDao

    companion object {
        const val DATABASE_NAME = "positions.db"
    }
}