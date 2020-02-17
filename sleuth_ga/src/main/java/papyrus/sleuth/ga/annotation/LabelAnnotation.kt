package papyrus.sleuth.ga.annotation

import android.util.Pair
import papyrus.sleuth.SleuthAnnotation
import papyrus.sleuth.ga.Params
import ext.fallback


@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Label(val value: String = "")

class LabelAnnotation : SleuthAnnotation() {

    override fun annotation(): Class<out Annotation> {
        return Label::class.java
    }

    override fun parseParam(annotation: Annotation, value: String?): List<Pair<String, String?>> {
        return listOf(Pair(Params.LABEL, (annotation as? Label)?.value?.fallback(value)))
    }
}