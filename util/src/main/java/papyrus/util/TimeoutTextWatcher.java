package papyrus.util;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.Timer;
import java.util.TimerTask;

public abstract class TimeoutTextWatcher implements TextWatcher {
    Timer timer;
    long timeout;

    public TimeoutTextWatcher(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(final Editable editable) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                textChanged(editable.toString().trim());
            }
        }, timeout);
    }

    public abstract void textChanged(String string);
}
