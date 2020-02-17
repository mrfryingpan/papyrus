package papyrus.adapter

import androidx.annotation.UiThread

abstract class DataItem<T>(val target: Int, item: T?, val type: Int) {
    var item: T? = item
        @UiThread
        set(value) {
            field = value
            observer?.onChanged(this)
        }
    var observer: ModuleObserver? = null

    val viewType: Int
        get() = item?.let { type } ?: -1


}