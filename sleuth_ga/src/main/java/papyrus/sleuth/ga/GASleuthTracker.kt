package papyrus.sleuth.ga

import com.google.android.gms.analytics.Tracker
import papyrus.sleuth.EventHandler
import papyrus.sleuth.SleuthAnnotation
import papyrus.sleuth.SleuthTracker
import papyrus.sleuth.ga.annotation.*

class GASleuthTracker(val handler: EventHandler, val verbose: Boolean = false) : SleuthTracker {
    constructor(tracker: Tracker, verbose: Boolean = false) : this(GAEventHandler(tracker), verbose)

    override fun eventHandler(): EventHandler {
        return this.handler
    }

    override fun supportedAnnotations(): List<SleuthAnnotation> {
        return listOf(GAScreenViewAnnotation(),
                GAEventAnnotation(),
                ActionAnnotation(),
                CategoryAnnotation(),
                LabelAnnotation(),
                ValueAnnotation(),
                CustomDimensionAnnotation(),
                CustomMetricAnnotation())
    }
}

