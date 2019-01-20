package nick.iamjob.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import nick.iamjob.ui.JobsFragment
import nick.iamjob.ui.NotificationsFragment
import nick.iamjob.ui.PositionFragment
import nick.iamjob.ui.SavedPositionsFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun jobsFragment(): JobsFragment

    @ContributesAndroidInjector
    abstract fun positionFragment(): PositionFragment

    @ContributesAndroidInjector
    abstract fun savedPositionsFragment(): SavedPositionsFragment

    @ContributesAndroidInjector
    abstract fun notificationsFragment(): NotificationsFragment
}