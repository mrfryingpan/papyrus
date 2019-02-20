package papyrus.demo;

import android.app.Application;

import papyrus.core.Papyrus;

public class App extends Application {
    private static App instance;


    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Papyrus.init(this);
    }

}
