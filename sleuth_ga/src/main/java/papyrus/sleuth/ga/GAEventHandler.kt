package papyrus.sleuth.ga

import android.util.Log
import com.google.android.gms.analytics.Tracker
import papyrus.sleuth.EventHandler
import papyrus.sleuth.SleuthEvent

class GAEventHandler(val tracker: Tracker, val verbose: Boolean = false) : EventHandler {
    override fun send(event: SleuthEvent) {
        this.track(this.configureEventMap(event))
    }

    protected fun track(event: Map<String, String>) {
        this.tracker.send(event)
    }

    protected fun configureEventMap(event: SleuthEvent): Map<String, String> {
        val map = event.toMap()
        val screenName = map.remove("SCREEN_NAME")
        map[Params.EVENT_TYPE] = screenName?.let {
            tracker.setScreenName(it)
            "screenview"
        } ?: "event"

        if (this.verbose) {
            val builder = StringBuilder("Screen Name: ").append(screenName)
            map.entries.forEach { entry ->
                builder.append("\n").append(entry.key).append(": ").append(entry.value)
            }

            Log.w("Sleuth", builder.toString())
        }

        return map
    }
}
