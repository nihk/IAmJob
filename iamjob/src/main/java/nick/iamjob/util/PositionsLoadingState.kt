package nick.iamjob.util

sealed class PositionsLoadingState {
    open class Done : PositionsLoadingState()
    open class Loading : PositionsLoadingState()
    object SimpleFetch : Loading()
    object DoneSimpleFetch : Done()
    object SwipeRefreshing : Loading()
    object DoneSwipeRefreshing : Done()
    object Paginating : Loading()
    object DonePaginating : Done()
}