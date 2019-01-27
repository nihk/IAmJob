package nick.iamjob.data

import nick.core.di.ApplicationScope
import nick.data.dao.SearchesDao
import javax.inject.Inject

@ApplicationScope
class SearchesRepository @Inject constructor(
    private val dao: SearchesDao
) {
}