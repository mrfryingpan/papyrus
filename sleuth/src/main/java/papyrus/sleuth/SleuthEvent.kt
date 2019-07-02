package papyrus.sleuth

import android.util.Pair
import java.util.*
import kotlin.collections.HashMap

class SleuthEvent {
    private val params = ArrayList<Pair<String, String?>>()

    fun toMap(): MutableMap<String, String> = params.fold(HashMap()) { event, param ->
        param.second?.let { value ->
            event[param.first] = value
        }
        event
    }

    fun addParams(_params: List<Pair<String, String?>>) {
        params.addAll(_params)
    }
}