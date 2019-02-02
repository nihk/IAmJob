package nick.data.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.Reusable
import nick.core.di.ApplicationContext
import nick.core.di.ApplicationScope
import nick.data.BuildConfig
import nick.data.JobsDatabase

@Module
object DataModule {

    @Reusable
    @Provides
    @JvmStatic
    fun sharedPreferences(@ApplicationContext applicationContext: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(applicationContext)

    @ApplicationScope
    @Provides
    @JvmStatic
    fun jobsDatabase(@ApplicationContext applicationContext: Context) =
        Room.databaseBuilder(
            applicationContext,
            JobsDatabase::class.java,
            JobsDatabase.DATABASE_NAME
        ).apply {
            if (BuildConfig.DEBUG) {
                setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            }
        }.build()

    @Reusable
    @Provides
    @JvmStatic
    fun positionsDao(database: JobsDatabase) = database.positionsDao()

    @Reusable
    @Provides
    @JvmStatic
    fun searchesDao(database: JobsDatabase) = database.searchesDao()
}