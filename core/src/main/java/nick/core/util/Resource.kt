package nick.core.util

sealed class Resource<out T> {
    object Loading : Resource<Unit>()
    data class Success<out T>(val data: T? = null) : Resource<T>()
    data class Error<out T>(val throwable: Throwable) : Resource<T>()
}