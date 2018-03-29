package papyrus.util;

import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static android.os.Process.THREAD_PRIORITY_DISPLAY;

public class PapyrusExecutor {
    private static Handler uiHandler = new Handler(Looper.getMainLooper());
    private static Executor uiExecutor = new Executor() {
        @Override
        public void execute(Runnable command) {
            uiHandler.post(command);
        }
    };
    private static Executor backgroundExecutor = Executors.newCachedThreadPool(new ThreadFactory() {
        @Override
        public Thread newThread(final Runnable r) {
            return new Thread(new Runnable() {
                @Override
                public void run() {
                    android.os.Process.setThreadPriority(THREAD_PRIORITY_DISPLAY);
                    r.run();
                }
            }, "Papyrus-Background");
        }
    });

    public static synchronized void ui(Runnable runnable) {
        uiExecutor.execute(runnable);
    }

    public static synchronized void background(Runnable runnable) {
        backgroundExecutor.execute(runnable);
    }

    public static synchronized void ui(final Runnable runnable, long timeout) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                uiExecutor.execute(runnable);
            }
        }, timeout);
    }

    public static synchronized void background(final Runnable runnable, long timeout) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                backgroundExecutor.execute(runnable);
            }
        }, timeout);
    }

    public static Executor ui() {
        return uiExecutor;
    }

    public static Executor background() {
        return backgroundExecutor;
    }
}
