package nick.networking

import io.reactivex.Single
import nick.data.model.Position
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubJobsService {

    companion object {
        const val BASE_URL = "https://jobs.github.com/"
    }

    @GET("positions.json")
    fun fetchPositions(
        @Query("description") description: String? = null,
        @Query("location") location: String? = null,
        @Query("lat") latitude: Double? = null,
        @Query("long") longitude: Double? = null,
        @Query("full_time") isFullTime: Boolean? = null,
        @Query("page") page: Int? = null
    ): Single<List<Position>>

    @GET("positions/{id}.json")
    fun fetchPosition(
        @Path("id") id: Int,
        @Query("markdown") markdown: Boolean? = true
    ): Single<Position>
}