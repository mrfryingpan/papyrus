package papyrus.sleuth.ga.annotation

import android.util.Pair
import papyrus.sleuth.SleuthAnnotation
import papyrus.sleuth.ga.Params
import ext.fallback


@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Value(val value: String = "")

class ValueAnnotation : SleuthAnnotation() {

    override fun annotation(): Class<out Annotation> {
        return Value::class.java
    }

    override fun parseParam(annotation: Annotation, value: String?): List<Pair<String, String?>> {
        return listOf(Pair(Params.VALUE, (annotation as? Value)?.value?.fallback(value)))
    }
}