package nick.networking.service

import io.reactivex.Single
import nick.data.model.Position
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubJobsService {

    companion object {
        const val BASE_URL = "https://jobs.github.com/"
    }

    @GET("positions")
    fun fetchPositions(
        @Query("description") description: String?,
        @Query("location") location: String?,
        @Query("page") page: Int? = 0
    ): Single<List<Position>>
}