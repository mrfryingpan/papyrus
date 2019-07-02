package papyrus.sleuth

import android.text.TextUtils
import android.util.Log
import papyrus.sleuth.annotation.BooleanParam
import papyrus.sleuth.annotation.FormattedParam
import papyrus.sleuth.annotation.JoinedParam
import papyrus.util.ext.either
import java.lang.IllegalArgumentException
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*

object Sleuth {
    private var annotations: MutableMap<Class<out Annotation>, SleuthAnnotation> = HashMap()
    private var trackers: MutableList<Any> = ArrayList()

    @Suppress("UNCHECKED_CAST")
    private fun <T> createTracker(clazz: Class<T>, handler: EventHandler): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf(clazz), object : InvocationHandler {
            override fun invoke(proxy: Any?, method: Method?, args: Array<Any>?): Any? {
                return if (method?.declaringClass == Any::class.java) {
                    method.invoke(this, args)
                } else {
                    method?.let { _method ->
                        args?.let { _args ->
                            this@Sleuth.parseEventAndFire(_method, _args, handler)
                        }
                    }
                    null
                }
            }
        }) as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf(clazz), object : InvocationHandler {
            override fun invoke(proxy: Any?, method: Method?, args: Array<Any>?): Any? {
                trackers.forEach { tracker ->
                    try {
                        method?.invoke(tracker, args)
                    } catch (e: IllegalArgumentException) {
                        //Ignore. This allows safe polymorphism without creating a mess in logs
                    } catch (e: Exception) {
                        Log.v("Sleuth", "Error", e)
                    }
                }
                return null
            }
        }) as T
    }

    private fun parseEventAndFire(method: Method, args: Array<Any>, handler: EventHandler) {
        Log.v("Sleuth", method.name)
        val event = SleuthEvent()

        method.annotations.forEach { annotation ->
            annotations[annotation.annotationClass.java]?.let { sleuthAnnotation ->
                event.addParams(sleuthAnnotation.parseParam(annotation, null))
            }
        }

        method.parameterAnnotations.forEachIndexed { paramIndex, paramAnnotations ->
            val value = performStringFormat(paramAnnotations, args[paramIndex])
            paramAnnotations.forEach { annotation ->
                annotations[annotation.annotationClass.java]?.let { sleuthAnnotation ->
                    event.addParams(sleuthAnnotation.parseParam(annotation, value))
                }
            }
        }

        handler.send(event)
    }

    private fun performStringFormat(parameterAnnotations: Array<Annotation>, value: Any?): String? {
        var booleanParam: BooleanParam? = null
        var formattedParam: FormattedParam? = null
        var joinedParam: JoinedParam? = null

        parameterAnnotations.forEach { annotation ->
            when (annotation) {
                is BooleanParam -> booleanParam = annotation
                is FormattedParam -> formattedParam = annotation
                is JoinedParam -> joinedParam = annotation
            }
        }

        var result: String? = booleanParam?.let { param ->
            (value as? Boolean?)?.either(param.onTrue, param.onFalse) as String?
        } ?: joinedParam?.let { param ->
            (value as? MutableIterable<Any?>)?.let {
                TextUtils.join(param.value, it)
            }
        } ?: value?.toString()

        formattedParam?.let { param ->
            result = String.format(param.value, result)
        }

        return result
    }

    fun addTracker(clazz: Class<*>, tracker: SleuthTracker): Sleuth {
        tracker.supportedAnnotations()?.forEach { annotation ->
            annotations[annotation.annotation()] = annotation
        }

        trackers.add(createTracker(clazz, tracker.eventHandler()))
        return this
    }
}