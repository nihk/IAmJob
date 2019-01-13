package nick.search.util

sealed class PositionsLoadingState {
    object SavingPosition : PositionsLoadingState()
    object DoneSavingPosition : PositionsLoadingState()
    object FetchingPositions : PositionsLoadingState()
    object DoneFetchingPositions : PositionsLoadingState()
}