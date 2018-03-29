package papyrus.demo;

import android.app.Application;

import papyrus.core.Papyrus;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Papyrus.init(this);
    }
}
