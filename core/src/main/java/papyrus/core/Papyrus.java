package papyrus.core;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import papyrus.core.iface.IPermissionRequester;
import papyrus.core.iface.IResultCallback;
import papyrus.core.navigation.Navigator;
import papyrus.core.permissions.PermissionRequest;
import papyrus.core.ui.activity.InterceptorActivity;
import papyrus.util.PapyrusExecutor;
import papyrus.util.PapyrusUtil;
import papyrus.util.Res;
import papyrus.util.WeakDelegate;

public class Papyrus {

    private static Papyrus instance;

    public static Papyrus init(Application app) {
        instance = new Papyrus();
        instance.appContext = app;
        instance.mConnectivityManager = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
        app.registerActivityLifecycleCallbacks(instance.lifecycleListener());
        Res.init(app);

        return instance;
    }

    private Application appContext;
    private AppCompatActivity currentActivity;
    private WeakReference<AppCompatActivity> activityInLimbo = new WeakReference<>(null);
    private ConnectivityManager mConnectivityManager;
    private SparseArray<PermissionRequest> permissionRequesters = new SparseArray<>();


    private Application.ActivityLifecycleCallbacks lifecycleListener() {
        return new PapyrusLifecycleListener();
    }

    private class PapyrusLifecycleListener implements Application.ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (AppCompatActivity.class.isAssignableFrom(activity.getClass())) {
                activityInLimbo = new WeakReference<>((AppCompatActivity) activity);
            } else {
                Log.w("Papyrus", String.format("%s does not inherit from AppCompatActivity.\nPapyrus may not function as expected.", activity.getClass().getCanonicalName()));
            }
        }


        @Override
        public void onActivityStarted(Activity activity) {
            if (AppCompatActivity.class.isAssignableFrom(activity.getClass())) {
                if (!PapyrusUtil.is_translucent(activity)) {
                    currentActivity = (AppCompatActivity) activity;
                }
                Activity ref = activityInLimbo.get();
                if (ref == currentActivity) {
                    activityInLimbo = new WeakReference<>(null);
                }
            } else {
                Log.w("Papyrus", String.format("%s does not inherit from AppCompatActivity.\nPapyrus may not function as expected.", activity.getClass().getCanonicalName()));
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (currentActivity == activity && !PapyrusUtil.is_translucent(activity)) {
                currentActivity = null;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

    public static Navigator navigate() {
        if (instance != null) {
            if (instance.currentActivity != null) {
                return new Navigator(instance.currentActivity);
            } else {
                return new Navigator(instance.appContext);
            }
        }

        return WeakDelegate.dummy(Navigator.class);
    }

    public static void finishCurrentActivity() {
        instance.currentActivity.finish();
    }

    public static void requestPermissions(final IPermissionRequester requester, final String... permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            PapyrusExecutor.ui(new Runnable() {
                @TargetApi(23)
                public void run() {
                    int inx_ = 0;
                    while (instance.permissionRequesters.get(inx_) != null) {
                        inx_++;
                    }
                    final int inx = inx_;
                    ArrayList<String> permissionsToRequest = new ArrayList<>();
                    List<String> permissionsGranted = new ArrayList<>();

                    for (String permission : permissions) {
                        switch (instance.appContext.checkSelfPermission(permission)) {
                            case PackageManager.PERMISSION_GRANTED:
                                permissionsGranted.add(permission);
                                break;
                            default:
                                permissionsToRequest.add(permission);
                                break;
                        }
                    }
                    if (!permissionsToRequest.isEmpty()) {
                        instance.permissionRequesters.put(inx, new PermissionRequest(requester, permissionsGranted));
                        navigate()
                                .action("requestPermissions")
                                .putStringArrayList("permissions", permissionsToRequest)
                                .onResult(new IResultCallback() {
                                    @Override
                                    public void onResult(int resultCode, Intent data) {
                                        String[] permissions = data.getStringArrayExtra("permissions");
                                        int[] grantResults = data.getIntArrayExtra("grantResults");
                                        PermissionRequest request = instance.permissionRequesters.get(inx);
                                        IPermissionRequester requester = request.requester;

                                        List<String> grantedPermissions = request.alreadyGranted;
                                        List<String> deniedPermissions = new ArrayList<>();

                                        for (int i = 0; i < permissions.length; i++) {
                                            switch (grantResults[i]) {
                                                case PackageManager.PERMISSION_GRANTED:
                                                    grantedPermissions.add(permissions[i]);
                                                    break;
                                                case PackageManager.PERMISSION_DENIED:
                                                    deniedPermissions.add(permissions[i]);
                                            }
                                        }

                                        instance.permissionRequesters.remove(inx);
                                        if (requester != null) {
                                            if (!PapyrusUtil.isEmpty(grantedPermissions)) {
                                                requester.onPermissionsGranted(grantedPermissions);
                                            }
                                            if (!PapyrusUtil.isEmpty(deniedPermissions)) {
                                                requester.onPermissionsDenied(deniedPermissions);
                                            }
                                        }
                                    }
                                })
                                .start(InterceptorActivity.class);
                    } else {
                        requester.onPermissionsGranted(permissionsGranted);
                    }
                }
            }, 250);
        } else {
            PapyrusExecutor.ui(new Runnable() {
                @Override
                public void run() {
                    requester.onPermissionsGranted(Arrays.asList(permissions));
                }
            });
        }
    }

    public static boolean shouldShowRational(String permission) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && instance.currentActivity.shouldShowRequestPermissionRationale(permission);
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkConnected() {
        NetworkInfo info = instance.mConnectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static void addFragmentToActiveBackstack(Fragment fragment, int containerID) {
        addFragmentToActiveBackstack(fragment, containerID, null);
    }

    private static AppCompatActivity getActiveActivity() {
        AppCompatActivity activeActivity = instance.activityInLimbo.get();
        if (activeActivity == null) {
            activeActivity = instance.currentActivity;
        }
        return activeActivity;
    }

    public static void addFragmentToActiveBackstack(Fragment fragment, int containerID, String name, int... animations) {
        AppCompatActivity activeActivity = getActiveActivity();
        if (activeActivity != null) {
            addFragmentToBackstack(activeActivity, fragment, containerID, name, animations);
        }
    }

    public static void addFragmentToBackstack(AppCompatActivity activity, Fragment fragment, int containerID, String name, int... animations) {
        if ("root".equalsIgnoreCase(name)) {
            throw new IllegalArgumentException("Fragment Cannot be named \"root\". This is reserved for Papyrus Use");
        }

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if (animations.length == 2) {
            transaction.setCustomAnimations(animations[0], animations[1]);
        } else if (animations.length == 4) {
            transaction.setCustomAnimations(animations[0], animations[1], animations[2], animations[3]);
        } else if (animations.length != 0) {
            throw new IllegalArgumentException("Animations array must have length of 0, 2, or 4");
        }
        transaction.replace(containerID, fragment);
        transaction.addToBackStack(name);
        transaction.commit();
    }

    public static boolean hasBackstack(AppCompatActivity activity) {
        return activity.getSupportFragmentManager().getBackStackEntryCount() > 0;
    }

    public static boolean popBackstack(AppCompatActivity activity) {
        return activity.getSupportFragmentManager().popBackStackImmediate();
    }

    public static boolean popBackstackTo(AppCompatActivity activity, String name) {
        return activity.getSupportFragmentManager().popBackStackImmediate(name, 0);
    }

    public static boolean popBackstackTo(String name) {
        AppCompatActivity activeActivity = getActiveActivity();
        return activeActivity != null && popBackstackTo(activeActivity, name);
    }

    public static boolean popBackstackIncluding(AppCompatActivity activity, String name) {
        return activity.getSupportFragmentManager().popBackStackImmediate(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static boolean popBackstackIncluding(String name) {
        AppCompatActivity activeActivity = getActiveActivity();
        return activeActivity != null && popBackstackIncluding(activeActivity, name);
    }

    public static void clearBackstack(AppCompatActivity activity) {
        while (hasBackstack(activity) && popBackstack(activity)) ;
    }

    public static void clearBackstack() {
        AppCompatActivity activeActivity = getActiveActivity();
        if (activeActivity != null) {
            clearBackstack(activeActivity);
        }
    }
}
