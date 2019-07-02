package papyrus.sleuth

import android.util.Pair

abstract class SleuthAnnotation {

    abstract fun annotation(): Class<out Annotation>

    abstract fun parseParam(annotation: Annotation, value: String?): List<Pair<String, String?>>
}
