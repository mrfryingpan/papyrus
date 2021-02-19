package papyrus.core.network

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import papyrus.core.iface.IAppInitializer
import papyrus.dsl.EmptyAction
import papyrus.util.PapyrusExecutor
import java.util.concurrent.LinkedBlockingDeque

@SuppressLint("MissingPermission")
object PapyrusNetwork : IAppInitializer {
    var connectivityManager: ConnectivityManager? = null
    val connectedNetworks = HashSet<Network>()
    val isConnected: Boolean
        @Suppress("DEPRECATION")
        get() = when (Build.VERSION.SDK_INT) {
            in 0 until Build.VERSION_CODES.LOLLIPOP ->
                connectivityManager?.activeNetworkInfo?.isConnected == true
            else -> connectedNetworks.isNotEmpty()
        }

    val monitors = LinkedBlockingDeque<NetworkMonitor>()

    override fun onAppCreated(app: Application) {
        connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager?.registerNetworkCallback(
                    NetworkRequest.Builder()
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            .build(),
                    callbackInternal
            )
        }
    }

    fun monitor(monitorConfigure: NetworkMonitor.() -> Unit) {
        monitors.add(NetworkMonitor().apply(monitorConfigure))
    }

    fun monitor(owner: LifecycleOwner, monitorConfigure: NetworkMonitor.() -> Unit) {
        NetworkMonitor().apply(monitorConfigure).also { monitor ->
            owner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                fun onLifecycleResume() {
                    monitors.offer(monitor)
                }

                @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                fun onLifecyclePause() {
                    monitors.remove(monitor)
                }
            })
        }
    }

    fun onReconnect(callback: () -> Unit) {
        NetworkMonitor().apply {
            onNetworkConnected {
                callback()
                PapyrusExecutor.background {
                    monitors.remove(this)
                }
            }
        }.also {
            monitors.offer(it)
        }
    }

    @SuppressLint("NewApi")
    val callbackInternal = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            val notify = connectedNetworks.isEmpty()
            connectedNetworks.add(network)
            if (notify) {
                monitors.forEach { it.onNetworkConnected() }
            }
        }

        override fun onLost(network: Network) {
            connectedNetworks.remove(network)
            if (connectedNetworks.isEmpty()) {
                monitors.forEach { it.onNetworkDisconnected() }
            }
        }
    }
}

class NetworkMonitor {
    val onNetworkConnected = EmptyAction()
    val onNetworkDisconnected = EmptyAction()
}