package papyrus.alerts

import android.util.SparseArray

const val ACTION_CANCEL = -1
const val ACTION_GENERIC = 0

class DialogCallbacks {
    val callbacks = SparseArray<(() -> Unit)>()
    val buttonIDs: IntArray
        get() = IntArray(callbacks.size()) { callbacks.keyAt(it) }

    var cancelID: Int? = null
        private set

    fun addCallback(id: Int, type: Int, action: () -> Unit) {
        callbacks.put(id, action)
        if (type == ACTION_CANCEL) {
            cancelID = id
        }
    }

    fun onResult(id: Int) {
        callbacks[id]?.invoke()
    }
}