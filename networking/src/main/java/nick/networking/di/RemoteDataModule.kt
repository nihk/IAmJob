package nick.networking.di

import dagger.Module
import dagger.Provides
import nick.core.di.ApplicationScope
import nick.networking.service.GitHubJobsService
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object RemoteDataModule {

    @Provides
    @JvmStatic
    fun retrofitBuilder() = Retrofit.Builder()

    @ApplicationScope
    @Provides
    @JvmStatic
    fun moshiConverterFactory(): MoshiConverterFactory = MoshiConverterFactory.create()

    @ApplicationScope
    @Provides
    @JvmStatic
    fun rxJava2CallAdapterFactory(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @ApplicationScope
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