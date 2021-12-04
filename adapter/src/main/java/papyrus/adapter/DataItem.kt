package papyrus.adapter

import androidx.annotation.UiThread

abstract class DataItem<T>(val target: Int, item: T?, val type: Int) {
    var item: T? = item
        @UiThread
        set(value) {
            if (field != value) {
                field = value
                observer?.onChanged(this)
            }
        }
    var observer: ModuleObserver? = null

    val viewType: Int
        get() = item?.let { type } ?: -1

    open val id: Int = target

    @Suppress("UNCHECKED_CAST")
    fun getChangePayload(newItem: DataItem<*>?): Any? {
        if (type == newItem?.type) {
            return (newItem as? DataItem<T>)?.let(this::calculateChange)
        }
        return null
    }

    open fun calculateChange(newItem: DataItem<T>): Any? = null
}