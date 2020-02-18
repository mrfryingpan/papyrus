package delegate

import android.annotation.SuppressLint
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

class WeakDelegate<T>(initialReferent: T? = null) {
    var backingField: WeakReference<T> = WeakReference<T>(initialReferent)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return backingField.get()
    }


    @SuppressLint("ApplySharedPref")
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        backingField = WeakReference<T>(value)
    }
}