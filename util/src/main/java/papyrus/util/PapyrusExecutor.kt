package papyrus.util

import android.os.Handler
import android.os.Looper
import android.os.Process
import android.util.Log
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object PapyrusExecutor {
    val uiHandler = Handler(Looper.getMainLooper())
    val ui = Executor { command -> uiHandler.post(StackTraceRunnable { command.run() }) }
    val background: Executor = Executors.newCachedThreadPool { r ->
        Thread(Runnable {
            Process.setThreadPriority(Process.THREAD_PRIORITY_DISPLAY)
            r.run()
        }, "Papyrus-Background")
    }

    @Synchronized
    fun ui(runnable: () -> Unit) {
        ui.execute(StackTraceRunnable { runnable() })
    }

    @Synchronized
    fun background(runnable: () -> Unit) {
        background.execute(StackTraceRunnable { runnable() })
    }

    @Synchronized
    fun ui(timeout: Long, runnable: () -> Unit) {
        val wrapped = StackTraceRunnable {
            runnable()
        }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                ui.execute(wrapped)
            }
        }, timeout)
    }

    @Synchronized
    fun background(timeout: Long, runnable: () -> Unit) {
        val wrapped = StackTraceRunnable {
            runnable()
        }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                background.execute(wrapped)
            }
        }, timeout)
    }
}

class StackTraceRunnable(val action: () -> Unit) : Runnable {
    val stackTrace = Thread.currentThread().stackTrace.let {
        it.takeLast(it.size - 3)
    }.joinToString("\n")

    override fun run() {
        try {
            action()
        } catch (e: Exception) {
            Log.e("PapyrusExecutor", "CAPTURED TRACE\n${stackTrace}")
            throw e
        }
    }
}