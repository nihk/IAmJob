package nick.data.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import nick.core.di.AppScope

@Module
object DataModule {

    @AppScope
    @Provides
    @JvmStatic
    fun sharedPreferences(applicationContext: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
}