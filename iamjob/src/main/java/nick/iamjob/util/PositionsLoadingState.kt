package nick.iamjob.util

sealed class PositionsLoadingState {
    object TabClickFetch : PositionsLoadingState()
    object TabClickDoneFetch : PositionsLoadingState()
    object SwipeRefreshFetch : PositionsLoadingState()
    object SwipeRefreshDoneFetch : PositionsLoadingState()
}