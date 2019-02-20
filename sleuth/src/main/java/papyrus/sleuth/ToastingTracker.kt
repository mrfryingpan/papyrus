package papyrus.sleuth

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast

class ToastingSleuthTracker(internal var app: Application, internal var verbose: Boolean) : SleuthTracker {

    override fun eventHandler(): EventHandler {
        return object : EventHandler {
            override fun send(event: SleuthEvent) {
                if (this@ToastingSleuthTracker.verbose) {
                    Handler(Looper.getMainLooper()).post {
                        val data = event.toMap().entries
                                .map { "${it.key}: ${it.value}\n" }
                                .fold(StringBuilder(), StringBuilder::append)

                        Toast.makeText(this@ToastingSleuthTracker.app, data.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun supportedAnnotations(): List<SleuthAnnotation>? {
        return null
    }
}
