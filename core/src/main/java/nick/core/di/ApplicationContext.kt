package nick.core.di

import javax.inject.Qualifier

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.LOCAL_VARIABLE
)
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationContext