package nick.data.util

sealed class PositionQuery {
    object SavedPositions : PositionQuery()
    object FreshPositions : PositionQuery()
}