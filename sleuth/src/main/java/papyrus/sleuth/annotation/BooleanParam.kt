package papyrus.sleuth.annotation

import kotlin.annotation.Target
import kotlin.annotation.Retention

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class BooleanParam(val onTrue: String = "NULL", val onFalse: String = "NULL")