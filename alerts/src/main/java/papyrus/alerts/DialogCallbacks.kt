package papyrus.alerts

import android.os.Bundle
import android.util.SparseArray

const val ACTION_CANCEL = -1
const val ACTION_GENERIC = 0

class DialogCallbacks {
    val callbacks = SparseArray<((Bundle) -> Unit)>()
    var fallback: ((Int, Bundle) -> Unit)? = null
    val buttonIDs: IntArray
        get() = IntArray(callbacks.size()) { callbacks.keyAt(it) }

    var cancelID: Int? = null
        private set

    fun addCallback(id: Int, type: Int, action: (Bundle) -> Unit) {
        callbacks.put(id, action)
        if (type == ACTION_CANCEL) {
            cancelID = id
        }
    }

    fun addFallback(action: (Int, Bundle) -> Unit) {
        fallback = action
    }

    fun onResult(id: Int, result: Bundle) {
        callbacks[id]?.invoke(result) ?: fallback?.invoke(id, result)
    }
}