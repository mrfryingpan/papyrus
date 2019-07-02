package papyrus.sleuth.ga.annotation

import android.util.Pair
import papyrus.sleuth.SleuthAnnotation
import papyrus.sleuth.ga.Params

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class GAScreenView(val value: String = "")

class GAScreenViewAnnotation : SleuthAnnotation() {
    override fun annotation(): Class<out Annotation> {
        return GAScreenView::class.java
    }

    override fun parseParam(annotation: Annotation, value: String?): List<Pair<String, String?>> {
        return listOf(Pair(Params.EVENT_TYPE, "screenview"), Pair(Params.SCREEN_NAME, (annotation as? GAScreenView)?.value))
    }
}