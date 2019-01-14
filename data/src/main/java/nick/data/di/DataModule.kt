package nick.data.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import nick.core.di.ApplicationContext
import nick.core.di.ApplicationScope
import nick.data.BuildConfig
import nick.data.PositionsDatabase

@Module
object DataModule {

    @ApplicationScope
    @Provides
    @JvmStatic
    fun sharedPreferences(applicationContext: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(applicationContext)

    @ApplicationScope
    @Provides
    @JvmStatic
    fun positionsDatabase(@ApplicationContext applicationContext: Context) =
        Room.databaseBuilder(
            applicationContext,
            PositionsDatabase::class.java,
            PositionsDatabase.DATABASE_NAME
        ).apply {
            if (BuildConfig.DEBUG) {
                this.setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            }
        }.build()

    @ApplicationScope
    @Provides
    @JvmStatic
    fun positionsDao(database: PositionsDatabase) = database.positionsDao()
}