package nick.iamjob.util

sealed class PositionsLoadingState {
    object FetchingPositions : PositionsLoadingState()
    object DoneFetchingPositions : PositionsLoadingState()
}