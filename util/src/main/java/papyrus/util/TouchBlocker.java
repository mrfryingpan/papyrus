package papyrus.util;

import android.view.MotionEvent;
import android.view.View;

public class TouchBlocker implements View.OnTouchListener {

    private static TouchBlocker instance;

    protected TouchBlocker() {

    }

    private static TouchBlocker get() {
        if (instance == null) {
            instance = new TouchBlocker();
        }
        return instance;
    }

    public static void block(View... views) {
        for (View view : views) {
            view.setOnTouchListener(get());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
