package nick.iamjob.util

sealed class PositionsLoadingState {
    object SimpleFetch : PositionsLoadingState()
    object SimpleDoneFetch : PositionsLoadingState()
    object SwipeRefreshFetch : PositionsLoadingState()
    object SwipeRefreshDoneFetch : PositionsLoadingState()
}