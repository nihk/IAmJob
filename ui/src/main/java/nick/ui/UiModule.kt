package nick.ui

import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
object UiModule {

    @Reusable
    @Provides
    @JvmStatic
    fun linkMovementMethod(): MovementMethod = LinkMovementMethod.getInstance()
}