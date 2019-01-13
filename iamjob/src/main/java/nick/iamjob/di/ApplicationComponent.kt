package nick.iamjob.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import nick.core.di.ApplicationScope
import nick.iamjob.IAmJobApplication

@ApplicationScope
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        ActivityModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<IAmJobApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}