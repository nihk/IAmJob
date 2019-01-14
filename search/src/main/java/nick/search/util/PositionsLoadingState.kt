package nick.search.util

sealed class PositionsLoadingState {
    object SavingOrUnsavingPosition : PositionsLoadingState()
    object DoneSavingOrUnsavingPosition : PositionsLoadingState()
    object FetchingPositions : PositionsLoadingState()
    object DoneFetchingPositions : PositionsLoadingState()
}