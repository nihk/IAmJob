package nick.iamjob.util

sealed class PositionsLoadingState {
    object SavingOrUnsavingPosition : PositionsLoadingState()
    object DoneSavingOrUnsavingPosition : PositionsLoadingState()
    object FetchingPositions : PositionsLoadingState()
    object DoneFetchingPositions : PositionsLoadingState()
}