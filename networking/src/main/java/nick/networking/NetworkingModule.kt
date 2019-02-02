package nick.networking

import android.content.Context
import android.net.ConnectivityManager
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import nick.core.di.ApplicationContext
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object NetworkingModule {

    @Reusable
    @Provides
    @JvmStatic
    fun connectivityManager(@ApplicationContext applicationContext: Context) =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @JvmStatic
    fun retrofitBuilder() = Retrofit.Builder()

    @Reusable
    @Provides
    @JvmStatic
    fun moshi(positionJsonAdapter: PositionJsonAdapter): Moshi = Moshi.Builder()
        .add(positionJsonAdapter)
        .build()

    @Reusable
    @Provides
    @JvmStatic
    fun moshiConverterFactory(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)

    @Reusable
    @Provides
    @JvmStatic
    fun rxJava2CallAdapterFactory(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Reusable
    @Provides
    @JvmStatic
    fun gitHubJobsService(
        builder: Retrofit.Builder,
        moshiConverterFactory: MoshiConverterFactory,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
    ): GitHubJobsService = createService(
        builder,
        moshiConverterFactory,
        rxJava2CallAdapterFactory,
        GitHubJobsService.BASE_URL,
        GitHubJobsService::class.java
    )

    private fun <T> createService(
        builder: Retrofit.Builder,
        converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory,
        baseUrl: String,
        clazz: Class<T>
    ): T = builder
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(callAdapterFactory)
        .baseUrl(baseUrl)
        .build()
        .create(clazz)
}