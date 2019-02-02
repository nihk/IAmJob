package nick.core.util

import dagger.Reusable
import javax.inject.Inject

@Reusable
class CurrentTime @Inject constructor() {

    fun inMillis() = System.currentTimeMillis()
}