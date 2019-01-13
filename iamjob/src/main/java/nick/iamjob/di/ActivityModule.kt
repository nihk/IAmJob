package nick.iamjob.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import nick.iamjob.ui.MainActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun mainActivity(): MainActivity
}