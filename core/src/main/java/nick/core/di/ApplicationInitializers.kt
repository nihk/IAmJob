package nick.core.di

import javax.inject.Inject

class ApplicationInitializers @Inject constructor(
    private val initializers: Set<@JvmSuppressWildcards Initializer>
): Initializer {

    override fun initialize() {
        initializers.forEach(Initializer::initialize)
    }
}