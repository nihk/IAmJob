package nick.search.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import nick.search.ui.PositionFragment
import nick.search.ui.SavedPositionsFragment
import nick.search.ui.SearchFragment

@Module
abstract class SearchUiModule {

    @ContributesAndroidInjector
    abstract fun searchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun positionFragment(): PositionFragment

    @ContributesAndroidInjector
    abstract fun savedPositionsFragment(): SavedPositionsFragment
}