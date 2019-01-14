package nick.iamjob.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import nick.iamjob.ui.PositionFragment
import nick.iamjob.ui.SavedPositionsFragment
import nick.iamjob.ui.JobsFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun searchFragment(): JobsFragment

    @ContributesAndroidInjector
    abstract fun positionFragment(): PositionFragment

    @ContributesAndroidInjector
    abstract fun savedPositionsFragment(): SavedPositionsFragment
}