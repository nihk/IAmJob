package nick.iamjob.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.Module
import dagger.Provides
import nick.core.di.AppScope

@Module
class AppModule {

    @AppScope
    @Provides
    fun processLifecycleOwner(): LifecycleOwner = ProcessLifecycleOwner.get()

    @AppScope
    @Provides
    fun processLifecycle(lifecycleOwner: LifecycleOwner) = lifecycleOwner.lifecycle

    @AppScope
    @Provides
    fun applicationContext(application: Application): Context = application.applicationContext

    @AppScope
    @Provides
    fun applicationResources(application: Application): Resources = application.resources

    @AppScope
    @Provides
    fun connectivityManager(applicationContext: Context) =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}