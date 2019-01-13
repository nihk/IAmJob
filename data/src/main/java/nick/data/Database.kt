package nick.data

import androidx.room.Database
import androidx.room.RoomDatabase
import nick.data.dao.PositionDao
import nick.data.model.Position

@Database(
    entities = [Position::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun positionDao(): PositionDao

    companion object {
        const val DATABASE_NAME = "search.db"
    }
}