package nick.iamjob.data

import android.content.SharedPreferences
import io.reactivex.Completable
import nick.core.di.ApplicationScope
import nick.data.dao.SearchesDao
import nick.data.model.Search
import javax.inject.Inject

@ApplicationScope
class SearchesRepository @Inject constructor(
    private val dao: SearchesDao,
    private val sharedPreferences: SharedPreferences
) {
    val searches = dao.queryAll()

    companion object {
        const val KEY_NOTIFICATION_FREQUENCY = "notification_frequency"
    }

    fun insert(search: Search): Completable = Completable.fromAction {
        dao.insert(search)
    }

    fun delete(search: Search): Completable = Completable.fromAction {
        dao.delete(search)
    }

    fun updateSearch(search: Search): Completable = Completable.fromAction {
        dao.update(search)
    }

    fun setNotificationFrequency(notificationFrequency: Int) {
        sharedPreferences.edit()
            .putInt(KEY_NOTIFICATION_FREQUENCY, notificationFrequency)
            .apply()
    }

    fun getNotificationFrequency(): Int = sharedPreferences.getInt(
        KEY_NOTIFICATION_FREQUENCY, 0
    )
}