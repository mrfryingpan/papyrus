package papyrus.util

import java.lang.ref.WeakReference
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

open class WeakDelegate {
    companion object {
        inline fun <reified T : Any> of(t: T): T {
            val tRef = WeakReference(t)
            return Proxy.newProxyInstance(T::class.java.classLoader, T::class.java.interfaces) { o, method, objects ->
                val t = tRef.get()
                if (t != null) {
                    method.invoke(t, *objects)
                } else {
                    val returnType = method.returnType
                    if (!returnType.isPrimitive) {
                        null
                    } else if (returnType == Boolean::class.javaPrimitiveType) {
                        false
                    } else {
                        0
                    }
                }
            } as T
        }

        fun <T> dummy(clazz: Class<T>): T {
            return Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz)) { o, method, objects -> null } as T
        }
    }
}
