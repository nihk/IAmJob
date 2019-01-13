package nick.core.util

import nick.core.di.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class CurrentTime @Inject constructor() {

    fun inMillis() = System.currentTimeMillis()
}