package nick.iamjob.ui

import nick.core.di.ApplicationScope
import nick.data.model.Search
import javax.inject.Inject

@ApplicationScope
class FilterStringFormatter @Inject constructor() {

    fun format(search: Search) = mutableListOf(
        search.description,
        search.location,
        if (search.isFullTime) "Full time" else ""
    ).also { it.removeAll { s -> s.isBlank() } }
        .joinToString(" | ")
}