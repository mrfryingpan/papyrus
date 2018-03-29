package papyrus.legacy;

import android.Manifest;
import android.app.Application;
import android.support.annotation.RequiresPermission;

import papyrus.core.Papyrus;


/**
 * @deprecated - Use papyrus.core.Papyrus Toolkit Directly
 */
public class PapyrusApp extends Application {

    private static PapyrusApp instance;

    public static PapyrusApp get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Papyrus.init(this);
        instance = this;
    }

    /**
     * @deprecated - Use Papyrus.isNetworkConnected Directly
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkConnected() {
        return Papyrus.isNetworkConnected();
    }


}
