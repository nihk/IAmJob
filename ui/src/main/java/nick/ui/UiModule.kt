package nick.ui

import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import dagger.Module
import dagger.Provides
import nick.core.di.ApplicationScope

@Module
object UiModule {

    @ApplicationScope
    @Provides
    @JvmStatic
    fun linkMovementMethod(): MovementMethod = LinkMovementMethod.getInstance()
}