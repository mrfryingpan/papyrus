package papyrus.alerts

import android.util.SparseArray

const val ACTION_CANCEL = -1
const val ACTION_GENERIC = 0

class DialogCallbacks(vararg callbacks: DialogCallback) {
    var cancelID: Int? = null
        private set

    private val callbackMap: SparseArray<(() -> Unit)> = SparseArray<(() -> Unit)>().apply {
        callbacks.forEach { callback ->
            put(callback.id, callback.action)
            if (callback.type == ACTION_CANCEL) {
                cancelID = callback.id
            }
        }
    }

    fun onResult(id: Int) {
        callbackMap[id].invoke()
    }
}

class DialogCallback(val id: Int, val type: Int = ACTION_GENERIC, val action: () -> Unit)

