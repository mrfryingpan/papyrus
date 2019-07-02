package papyrus.sleuth.ga.annotation

import android.text.TextUtils
import android.util.Pair
import papyrus.sleuth.SleuthAnnotation
import papyrus.sleuth.ga.Params
import java.util.ArrayList

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class GAEvent(val category: String = "", val action: String = "")

class GAEventAnnotation : SleuthAnnotation() {
    override fun annotation(): Class<out Annotation> {
        return GAEvent::class.java
    }

    override fun parseParam(annotation: Annotation, value: String?): List<Pair<String, String?>> {
        val params = ArrayList<Pair<String, String?>>()
        (annotation as? GAEvent)?.let { instance ->
            val category = instance.category
            val action = instance.action

            if (!TextUtils.isEmpty(category)) {
                params.add(Pair(Params.CATEGORY, category))
            }

            if (!TextUtils.isEmpty(action)) {
                params.add(Pair(Params.ACTION, action))
            }
        }
        return params
    }
}