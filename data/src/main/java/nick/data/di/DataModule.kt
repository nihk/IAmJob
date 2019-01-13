package nick.data.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import nick.core.di.ApplicationScope

@Module
object DataModule {

    @ApplicationScope
    @Provides
    @JvmStatic
    fun sharedPreferences(applicationContext: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
}