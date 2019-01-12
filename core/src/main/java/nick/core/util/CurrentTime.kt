package nick.core.util

import nick.core.di.AppScope
import javax.inject.Inject

@AppScope
class CurrentTime @Inject constructor() {

    fun inMillis() = System.currentTimeMillis()
}