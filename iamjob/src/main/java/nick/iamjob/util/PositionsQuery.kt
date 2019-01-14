package nick.iamjob.util

sealed class PositionsQuery {
    object SavedPositions : PositionsQuery()
    object FreshPositions : PositionsQuery()
}