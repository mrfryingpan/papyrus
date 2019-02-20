package papyrus.sleuth.ga.annotation

import android.util.Pair
import papyrus.sleuth.SleuthAnnotation
import papyrus.sleuth.ga.Params
import papyrus.util.ext.fallback
import java.util.*


@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomDimension(val index: Int, val value: String = "")

class CustomDimensionAnnotation : SleuthAnnotation() {

    override fun annotation(): Class<out Annotation> {
        return CustomDimension::class.java
    }

    override fun parseParam(annotation: Annotation, value: String?): List<Pair<String, String?>> {
        return (annotation as? CustomDimension)?.let { instance ->
            listOf(Pair(Params.customDimension(instance.index), instance.value.fallback(value)))
        } ?: listOf()
    }
}