package nick.iamjob.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun processLifecycleOwner(): LifecycleOwner = ProcessLifecycleOwner.get()

    @Singleton
    @Provides
    fun processLifecycle(lifecycleOwner: LifecycleOwner) = lifecycleOwner.lifecycle

    @Singleton
    @Provides
    fun applicationContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun applicationResources(application: Application): Resources = application.resources

    @Singleton
    @Provides
    fun connectivityManager(applicationContext: Context) =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}