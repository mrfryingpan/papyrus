package papyrus.util

import java.lang.ref.WeakReference
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

@Suppress("UNCHECKED_CAST")
open class WeakDelegate {
    companion object {
        inline fun <reified T : Any> of(t: T): T {
            val tRef = WeakReference(t)
            return Proxy.newProxyInstance(T::class.java.classLoader, T::class.java.interfaces) { _, method, objects ->
                val instance = tRef.get()
                if (instance != null) {
                    method.invoke(instance, *objects)
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
            return Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz)) { _, _, _ -> null } as T
        }
    }
}
