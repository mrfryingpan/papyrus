package papyrus.sleuth.ga.annotation

import android.util.Pair
import papyrus.sleuth.SleuthAnnotation
import papyrus.sleuth.ga.Params
import papyrus.util.ext.fallback


@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Category(val value: String = "")

class CategoryAnnotation : SleuthAnnotation() {

    override fun annotation(): Class<out Annotation> {
        return Category::class.java
    }

    override fun parseParam(annotation: Annotation, value: String?): List<Pair<String, String?>> {
        return listOf(Pair(Params.CATEGORY, (annotation as? Category)?.value?.fallback(value)))
    }
}