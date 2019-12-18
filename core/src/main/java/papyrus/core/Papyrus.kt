package papyrus.core

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import android.util.Log
import android.util.SparseArray

import java.lang.ref.WeakReference
import java.util.ArrayList

import papyrus.core.navigation.Navigator
import papyrus.core.permissions.PermissionRequest
import papyrus.core.ui.activity.InterceptorActivity
import papyrus.util.PapyrusExecutor
import papyrus.util.PapyrusUtil
import papyrus.util.Res
import papyrus.util.WeakDelegate

typealias IPermissionCallback = (granted: List<String>?, denied: List<String>?) -> Unit

class Papyrus(val appContext: Application, val connectivityManager: ConnectivityManager) {
    private var currentActivity: AppCompatActivity? = null
    private var activityInLimbo: WeakReference<AppCompatActivity?> = WeakReference(null)
    private val permissionRequesters = SparseArray<PermissionRequest>()

    private fun lifecycleListener(): Application.ActivityLifecycleCallbacks {
        return PapyrusLifecycleListener()
    }

    private inner class PapyrusLifecycleListener : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (AppCompatActivity::class.java.isAssignableFrom(activity.javaClass)) {
                activityInLimbo = WeakReference(activity as AppCompatActivity)
            } else {
                Log.w("Papyrus", String.format("%s does not inherit from AppCompatActivity.\nPapyrus may not function as expected.", activity.javaClass.canonicalName))
            }
        }


        override fun onActivityStarted(activity: Activity) {
            if (AppCompatActivity::class.java.isAssignableFrom(activity.javaClass)) {
                if (!PapyrusUtil.is_translucent(activity)) {
                    currentActivity = activity as AppCompatActivity
                }
                if (activityInLimbo.get() === currentActivity) {
                    activityInLimbo = WeakReference(null)
                }
            } else {
                Log.w("Papyrus", String.format("%s does not inherit from AppCompatActivity.\nPapyrus may not function as expected.", activity.javaClass.canonicalName))
            }
        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {
            if (currentActivity === activity && !PapyrusUtil.is_translucent(activity)) {
                currentActivity = null
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {

        }
    }

    companion object {

        private var instance: Papyrus? = null

        fun init(app: Application): Papyrus {
            return Papyrus(app, app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).also {
                instance = it
                app.registerActivityLifecycleCallbacks(it.lifecycleListener())
                Res.init(app)
            }
        }

        fun navigate(): Navigator {
            return instance?.currentActivity?.let { Navigator(it) }
                    ?: instance?.appContext?.let { Navigator(it) }
                    ?: WeakDelegate.dummy(Navigator::class.java)
        }

        fun finishCurrentActivity() {
            instance?.currentActivity?.finish()
        }

        fun requestPermissions(vararg permissions: String, callback: IPermissionCallback) {
            if (Build.VERSION.SDK_INT >= 23) {
                PapyrusExecutor.ui(250L) {
                    var inx = 0
                    while (instance?.permissionRequesters?.get(inx) != null) {
                        inx++
                    }
                    val permissionsToRequest = ArrayList<String>()
                    val permissionsGranted = ArrayList<String>()

                    for (permission in permissions) {
                        when (instance?.appContext?.checkSelfPermission(permission)) {
                            PackageManager.PERMISSION_GRANTED -> permissionsGranted.add(permission)
                            else -> permissionsToRequest.add(permission)
                        }
                    }
                    if (permissionsToRequest.isNotEmpty()) {
                        instance?.permissionRequesters?.put(inx, PermissionRequest(callback, permissionsGranted))
                        navigate()
                                .action("requestPermissions")
                                .putStringArrayList("permissions", permissionsToRequest)
                                .onResult { _, _data ->
                                    _data?.let { data ->
                                        val permissions = data.getStringArrayExtra("permissions")
                                        val grantResults = data.getIntArrayExtra("grantResults")
                                        val request = instance?.permissionRequesters?.get(inx)
                                        instance?.permissionRequesters?.remove(inx)

                                        val granted = request?.alreadyGranted ?: ArrayList()
                                        val denied = ArrayList<String>()

                                        permissions.indices.forEach { i ->
                                            when (grantResults[i]) {
                                                PackageManager.PERMISSION_GRANTED -> granted.add(permissions[i])
                                                PackageManager.PERMISSION_DENIED -> denied.add(permissions[i])
                                            }
                                        }

                                        request?.callback?.invoke(granted, denied)
                                    }
                                }
                                .start(InterceptorActivity::class.java)
                    } else {
                        callback(permissionsGranted, null)
                    }
                }
            } else {
                PapyrusExecutor.ui { callback(arrayListOf(*permissions), null) }
            }
        }

        fun shouldShowRational(permission: String): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && instance?.currentActivity?.shouldShowRequestPermissionRationale(permission) == true
        }

        val isNetworkConnected: Boolean
            @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
            get() = instance?.connectivityManager?.activeNetworkInfo?.isConnected == true

        fun addFragmentToActiveBackstack(fragment: Fragment, containerID: Int) {
            addFragmentToActiveBackstack(fragment, containerID, null)
        }

        private val activeActivity: AppCompatActivity?
            get() = instance?.activityInLimbo?.get() ?: instance?.currentActivity

        fun addFragmentToActiveBackstack(fragment: Fragment, containerID: Int, name: String?, vararg animations: Int) {
            val activeActivity = activeActivity
            if (activeActivity != null) {
                addFragmentToBackstack(activeActivity, fragment, containerID, name, *animations)
            }
        }

        fun addFragmentToBackstack(activity: AppCompatActivity, fragment: Fragment, containerID: Int, name: String?, vararg animations: Int) {
            if ("root".equals(name, ignoreCase = true)) {
                throw IllegalArgumentException("Fragment Cannot be named \"root\". This is reserved for Papyrus Use")
            }

            val transaction = activity.supportFragmentManager.beginTransaction()
            when {
                animations.size == 2 -> transaction.setCustomAnimations(animations[0], animations[1])
                animations.size == 4 -> transaction.setCustomAnimations(animations[0], animations[1], animations[2], animations[3])
                animations.isNotEmpty() -> throw IllegalArgumentException("Animations array must have length of 0, 2, or 4")
            }
            transaction.replace(containerID, fragment)
            transaction.addToBackStack(name)
            transaction.commit()
        }

        fun hasBackstack(activity: AppCompatActivity): Boolean {
            return activity.supportFragmentManager.backStackEntryCount > 0
        }

        fun popBackstack(activity: AppCompatActivity): Boolean {
            return activity.supportFragmentManager.popBackStackImmediate()
        }

        fun popBackstackTo(activity: AppCompatActivity, name: String): Boolean {
            return activity.supportFragmentManager.popBackStackImmediate(name, 0)
        }

        fun popBackstackTo(name: String): Boolean {
            val activeActivity = activeActivity
            return activeActivity != null && popBackstackTo(activeActivity, name)
        }

        fun popBackstackIncluding(activity: AppCompatActivity, name: String): Boolean {
            return activity.supportFragmentManager.popBackStackImmediate(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        fun popBackstackIncluding(name: String): Boolean {
            val activeActivity = activeActivity
            return activeActivity != null && popBackstackIncluding(activeActivity, name)
        }

        fun clearBackstack(activity: AppCompatActivity) {
            while (hasBackstack(activity) && popBackstack(activity));
        }

        fun clearBackstack() {
            activeActivity?.let {
                clearBackstack(it)
            }
        }
    }
}
