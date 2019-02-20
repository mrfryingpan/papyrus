package papyrus.sleuth.annotation


import kotlin.annotation.Target
import kotlin.annotation.Retention

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class FormattedParam(val value: String = "%s")