package nick.search.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import nick.search.ui.PositionFragment
import nick.search.ui.SavedResultsFragment
import nick.search.ui.SearchFragment
import nick.search.ui.SearchResultsFragment

@Module
abstract class SearchUiModule {

    @ContributesAndroidInjector
    abstract fun searchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun searchResultsFragment(): SearchResultsFragment

    @ContributesAndroidInjector
    abstract fun positionFragment(): PositionFragment

    @ContributesAndroidInjector
    abstract fun savedResultsFragment(): SavedResultsFragment
}