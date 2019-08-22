package papyrus.core.permissions

import android.content.Context

import java.lang.ref.WeakReference

import papyrus.core.iface.IPermissionRequester
import papyrus.util.WeakDelegate

class PermissionRequest(requester: IPermissionRequester, var alreadyGranted: List<String>) {
    var requester: IPermissionRequester

    init {
        if (Context::class.java.isAssignableFrom(requester.javaClass)) {
            this.requester = WeakDelegate.of(requester)
        } else {
            this.requester = requester
        }
    }
}
