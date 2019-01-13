package nick.data.model

import androidx.room.*

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(tableName = SavedPosition.TABLE_NAME)
data class SavedPosition(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COL_ID)
    val id: Long = 0L,
    @Embedded(prefix = COL_POSITION_PREFIX)
    val position: Position
) {

    companion object {
        const val TABLE_NAME = "saved_positons"
        const val COL_ID = "id"
        const val COL_POSITION_PREFIX = "position"
    }
}