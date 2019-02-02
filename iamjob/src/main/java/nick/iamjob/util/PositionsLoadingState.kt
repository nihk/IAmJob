package nick.iamjob.util

sealed class PositionsLoadingState {
    open class Done : PositionsLoadingState()
    open class Loading : PositionsLoadingState()
    object SimpleFetch : Loading(), ReplacesData
    object DoneSimpleFetch : Done(), ReplacesData
    object SwipeRefreshing : Loading(), ReplacesData
    object DoneSwipeRefreshing : Done(), ReplacesData
    object Paginating : Loading(), AppendsData
    object DonePaginating : Done(), AppendsData
}

interface ReplacesData
interface AppendsData