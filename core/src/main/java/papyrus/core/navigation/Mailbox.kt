package papyrus.core.navigation

import android.os.Parcel
import android.util.SparseArray

object Mailbox {
    val inventory = SparseArray<Any>()

    inline fun <reified T : Any> ship(obj: T): Int = synchronized(inventory) {
        var i = 0
        while (inventory.get(i) != null) {
            i++
        }
        inventory.put(i, obj)
        return i
    }

    inline fun <reified T : Any> deliver(i: Int): T = synchronized(inventory) {
        val obj = inventory.get(i) as T
        inventory.remove(i)
        return obj
    }
}

inline fun <reified T : Any> Parcel.writeArbitrary(obj: T) = writeInt(Mailbox.ship(obj))
inline fun <reified T : Any> Parcel.readArbitrary(): T = Mailbox.deliver(readInt())