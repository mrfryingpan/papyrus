package papyrus.util

import android.os.Handler
import android.os.Looper
import android.os.Process
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object PapyrusExecutor {
    val uiHandler = Handler(Looper.getMainLooper())
    val ui = Executor { command -> uiHandler.post(command) }
    val background: Executor = Executors.newCachedThreadPool { r ->
        Thread(Runnable {
            Process.setThreadPriority(Process.THREAD_PRIORITY_DISPLAY)
            r.run()
        }, "Papyrus-Background")
    }

    @Synchronized
    fun ui(runnable: () -> Unit) {
        ui.execute(runnable)
    }

    @Synchronized
    fun background(runnable: () -> Unit) {
        background.execute(runnable)
    }

    @Synchronized
    fun ui(timeout: Long, runnable: () -> Unit) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                ui.execute(runnable)
            }
        }, timeout)
    }

    @Synchronized
    fun background(timeout: Long, runnable: () -> Unit) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                background.execute(runnable)
            }
        }, timeout)
    }
}