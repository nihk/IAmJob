package nick.iamjob.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import nick.iamjob.ui.*

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

    @ContributesAndroidInjector
    abstract fun errorDialogFragment(): ErrorDialogFragment
}