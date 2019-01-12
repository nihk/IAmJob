package nick.core.util

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentTime @Inject constructor() {

    fun inMillis() = System.currentTimeMillis()
}