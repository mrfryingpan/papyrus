package papyrus.adapter

import androidx.annotation.UiThread

abstract class DataItem<T>(val target: Int, item: T?, val type: Int, val priority: Int = -1) {
    var item: T? = item
        @UiThread
        set(value) {
            field = value
            observer?.onChanged(this)
        }
    var observer: ModuleObserver? = null

    val viewType: Int
        get() = item?.let { type } ?: -1

    @Suppress("UNCHECKED_CAST")
    fun getChangePayload(newItem: DataItem<*>?): Any? {
        if (type == newItem?.type) {
            return (newItem as? DataItem<T>)?.let(this::calculateChange)
        }
        return null
    }

    open fun calculateChange(newItem: DataItem<T>): Any? = null
}