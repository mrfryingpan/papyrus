package ext

import android.view.View

@Suppress("UNCHECKED_CAST")
fun <T> View.findParentWithID(id: Int): T? {
    return if (this.id == id) {
        this as T
    } else {
        (parent as? View)?.findParentWithID<T>(id)
    }
}