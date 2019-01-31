package nick.iamjob.data

import io.reactivex.Completable
import nick.core.di.ApplicationScope
import nick.data.dao.SearchesDao
import nick.data.model.Search
import javax.inject.Inject

@ApplicationScope
class SearchesRepository @Inject constructor(
    private val dao: SearchesDao
) {
    val searches = dao.queryAll()

    fun insert(search: Search): Completable = Completable.fromAction {
        dao.insert(search)
    }

    fun delete(search: Search): Completable = Completable.fromAction {
        dao.delete(search)
    }

    fun updateSearch(search: Search): Completable = Completable.fromAction {
        dao.update(search)
    }
}