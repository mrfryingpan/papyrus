package papyrus.core.network

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import papyrus.core.iface.IAppInitializer
import papyrus.dsl.EmptyAction
import java.util.*
import kotlin.collections.HashSet

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

    val monitors = LinkedList<NetworkMonitor>()

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