package papyrus.sleuth.ga.annotation

import android.util.Pair
import papyrus.sleuth.SleuthAnnotation
import papyrus.sleuth.ga.Params
import ext.fallback


@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomMetric(val index: Int, val value: String = "")

class CustomMetricAnnotation : SleuthAnnotation() {

    override fun annotation(): Class<out Annotation> {
        return CustomMetric::class.java
    }

    override fun parseParam(annotation: Annotation, value: String?): List<Pair<String, String?>> {
        return (annotation as? CustomMetric)?.let { instance ->
            listOf(Pair(Params.customMetric(instance.index), instance.value.fallback(value)))
        } ?: listOf()
    }
}