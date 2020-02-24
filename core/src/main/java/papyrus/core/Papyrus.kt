package papyrus.core

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import papyrus.core.iface.IAppInitializer
import papyrus.core.navigation.Navigator
import papyrus.core.permissions.PermissionRequest
import papyrus.util.PapyrusExecutor
import papyrus.util.PapyrusUtil
import papyrus.util.Res
import java.lang.ref.WeakReference

typealias IPermissionCallback = (granted: List<String>?, denied: List<String>?) -> Unit

@Suppress("UNUSED", "MemberVisibilityCanBePrivate")
object Papyrus {
    lateinit var app: Application
    lateinit var connectivityManager: ConnectivityManager
    private var currentActivity: Activity? = null
    private var activityInLimbo: WeakReference<Activity?> = WeakReference(null)
    private val permissionRequesters = SparseArray<PermissionRequest>()

    private val activeActivity: Activity?
        get() = activityInLimbo.get() ?: currentActivity

    val isNetworkConnected: Boolean
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        get() = connectivityManager.activeNetworkInfo.isConnected

    fun init(app: Application, vararg initializers: IAppInitializer) {
        this.app = app
        connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        Res.init(app)

        initializers.forEach {
            it.onAppCreated(app)
        }
        app.registerActivityLifecycleCallbacks(PapyrusLifecycleListener())
    }

    fun navigate(): Navigator = currentActivity?.let { Navigator(it) }
            ?: Navigator(app)

    fun finishCurrentActivity() = currentActivity?.finish()

    fun requestPermissions(vararg requestedPermissions: String, callback: IPermissionCallback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            PapyrusExecutor.ui { callback(arrayListOf(*requestedPermissions), null) }
        } else {
            PapyrusExecutor.ui(250L) {
                var requestCode = 0
                while (permissionRequesters.get(requestCode) != null) {
                    requestCode++
                }
                val permissionsNeeded = ArrayList<String>()
                val permissionsGranted = ArrayList<String>()
                requestedPermissions.forEach { permission ->
                    when (app.checkSelfPermission(permission)) {
                        PackageManager.PERMISSION_GRANTED -> permissionsGranted.add(permission)
                        PackageManager.PERMISSION_DENIED -> permissionsNeeded.add(permission)
                    }
                }

                if (permissionsNeeded.isEmpty()) {
                    callback(permissionsGranted, null)
                } else {
                    permissionRequesters.put(requestCode, PermissionRequest(callback, permissionsGranted))
                    navigate()
                            .action("requestPermissions")
                            .putStringArrayList("permissions", permissionsNeeded)
                            .onResult { _, _data ->
                                _data?.let { data ->
                                    val permissions = data.getStringArrayExtra("permissions")
                                    val grantResults = data.getIntArrayExtra("grantResults")
                                    val request = permissionRequesters.get(requestCode)
                                    permissionRequesters.remove(requestCode)

                                    val granted = request.alreadyGranted
                                    val denied = ArrayList<String>()

                                    permissions.forEachIndexed { index, permission ->
                                        when (grantResults[index]) {
                                            PackageManager.PERMISSION_GRANTED -> granted.add(permission)
                                            PackageManager.PERMISSION_DENIED -> denied.add(permission)
                                        }
                                    }

                                    request.callback(granted, denied)
                                }
                            }
                }
            }
        }
    }

    fun shouldShowRational(permission: String) = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && currentActivity?.shouldShowRequestPermissionRationale(permission) == true

    fun addFragmentToActiveBackstack(fragment: Fragment, containerID: Int) =
            addFragmentToActiveBackstack(fragment, containerID, null)


    fun addFragmentToActiveBackstack(fragment: Fragment, containerID: Int, name: String?, vararg animations: Int) =
            (activeActivity as? AppCompatActivity)?.let {
                addFragmentToBackstack(it, fragment, containerID, name, *animations)
            }


    fun addFragmentToBackstack(activity: AppCompatActivity, fragment: Fragment, containerID: Int, name: String?, vararg animations: Int) {
        if ("root".equals(name, ignoreCase = true))
            throw IllegalArgumentException("Fragment Cannot be named \"root\". This is reserved for Papyrus Use")

        activity.supportFragmentManager.beginTransaction()
                .also { transaction ->
                    when {
                        animations.size == 2 -> transaction.setCustomAnimations(animations[0], animations[1])
                        animations.size == 4 -> transaction.setCustomAnimations(animations[0], animations[1], animations[2], animations[3])
                        animations.isNotEmpty() -> throw IllegalArgumentException("Animations array must have length of 0, 2, or 4")
                    }
                    transaction.replace(containerID, fragment)
                    transaction.addToBackStack(name)

                }.commit()
    }

    fun hasBackstack(activity: AppCompatActivity): Boolean =
            activity.supportFragmentManager.backStackEntryCount > 0

    fun popBackstack(activity: AppCompatActivity): Boolean =
            activity.supportFragmentManager.popBackStackImmediate()

    fun popBackstackTo(activity: AppCompatActivity, name: String): Boolean =
            activity.supportFragmentManager.popBackStackImmediate(name, 0)

    fun popBackstackTo(name: String): Boolean = (activeActivity as? AppCompatActivity)?.let {
        popBackstackTo(it, name)
    } == true

    fun popBackstackIncluding(activity: AppCompatActivity, name: String): Boolean =
            activity.supportFragmentManager.popBackStackImmediate(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    fun popBackstackIncluding(name: String): Boolean = (activeActivity as? AppCompatActivity)?.let {
        popBackstackIncluding(it, name)
    } == true

    @Suppress("ControlFlowWithEmptyBody")
    fun clearBackstack(activity: AppCompatActivity) {
        while (hasBackstack(activity) && popBackstack(activity));
    }

    fun clearBackstack() = (activeActivity as? AppCompatActivity)?.let {
        clearBackstack(it)
    }


    private class PapyrusLifecycleListener : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            activityInLimbo = WeakReference(activity)
        }


        override fun onActivityStarted(activity: Activity) {
            if (!PapyrusUtil.is_translucent(activity)) {
                currentActivity = activity
            }
            if (activityInLimbo.get() == currentActivity) {
                activityInLimbo = WeakReference(null)
            }
        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {
            if (currentActivity == activity && !PapyrusUtil.is_translucent(activity)) {
                currentActivity = null
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {

        }
    }
}

