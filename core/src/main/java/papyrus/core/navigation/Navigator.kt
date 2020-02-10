package papyrus.core.navigation

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.*
import papyrus.core.ui.activity.InterceptorActivity
import papyrus.util.WeakDelegate
import java.io.Serializable
import java.util.*
import kotlin.reflect.KClass

typealias IResultCallback = (resultCode: Int, data: Intent?) -> Unit

class Navigator {
    internal var isActivity: Boolean = false
    internal var context: Context
    internal var flags: MutableSet<Int> = HashSet()
    internal var extras = Bundle()
    internal var customizer: Customizer? = WeakDelegate.dummy(Customizer::class.java)
    internal var action: String? = null
    internal var data: Uri? = null
    internal var resultCallback: IResultCallback? = null

    constructor(activity: Activity) {
        context = activity
        isActivity = true
    }

    constructor(context: Context) {
        this.context = context
        flags.add(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    fun flags(vararg newFlags: Int): Navigator {
        Collections.addAll(flags, *newFlags.toTypedArray())
        return this
    }

    fun action(action: String): Navigator {
        this.action = action
        return this
    }

    fun data(data: Uri): Navigator {
        this.data = data
        return this
    }

    fun putAll(bundle: Bundle?): Navigator {
        bundle?.let(extras::putAll)
        return this
    }

    fun putBoolean(key: String, value: Boolean): Navigator {
        extras.putBoolean(key, value)
        return this
    }

    fun putByte(key: String, value: Byte): Navigator {
        extras.putByte(key, value)
        return this
    }

    fun putChar(key: String, value: Char): Navigator {
        extras.putChar(key, value)
        return this
    }

    fun putShort(key: String, value: Short): Navigator {
        extras.putShort(key, value)
        return this
    }

    fun putFloat(key: String, value: Float): Navigator {
        extras.putFloat(key, value)
        return this
    }

    fun putCharSequence(key: String, value: CharSequence): Navigator {
        extras.putCharSequence(key, value)
        return this
    }

    fun putParcelable(key: String, value: Parcelable): Navigator {
        extras.putParcelable(key, value)
        return this
    }

    fun putParcelableArray(key: String, value: Array<Parcelable>): Navigator {
        extras.putParcelableArray(key, value)
        return this
    }

    fun putIntegerArrayList(key: String, value: ArrayList<Int>): Navigator {
        extras.putIntegerArrayList(key, value)
        return this
    }

    fun putString(key: String, value: String): Navigator {
        extras.putString(key, value)
        return this
    }

    fun putStringArrayList(key: String, value: ArrayList<String>): Navigator {
        extras.putStringArrayList(key, value)
        return this
    }

    fun putSerializable(key: String, value: Serializable): Navigator {
        extras.putSerializable(key, value)
        return this
    }

    fun putByteArray(key: String, value: ByteArray): Navigator {
        extras.putByteArray(key, value)
        return this
    }

    fun putShortArray(key: String, value: ShortArray): Navigator {
        extras.putShortArray(key, value)
        return this
    }

    fun putCharArray(key: String, value: CharArray): Navigator {
        extras.putCharArray(key, value)
        return this
    }

    fun putFloatArray(key: String, value: FloatArray): Navigator {
        extras.putFloatArray(key, value)
        return this
    }

    fun putCharSequenceArray(key: String, value: Array<CharSequence>): Navigator {
        extras.putCharSequenceArray(key, value)
        return this
    }

    fun putBundle(key: String, value: Bundle): Navigator {
        extras.putBundle(key, value)
        return this
    }

    fun withCustomization(customizer: Customizer?): Navigator {
        if (customizer != null) {
            this.customizer = customizer
        }
        return this
    }

    fun clearTask(): Navigator {
        flags.add(Intent.FLAG_ACTIVITY_NEW_TASK)
        flags.add(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        return this
    }

    fun forwardResult(): Navigator {
        flags.add(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
        return this
    }

    fun onResult(resultCallback: IResultCallback): Navigator {
        this.resultCallback = resultCallback
        return this
    }

    private fun prepareIntent(destination: Class<*>): Intent {
        return this.prepareIntent(Intent(context, destination))
    }

    private fun prepareIntent(intent: Intent): Intent {
        intent.flags = flags.fold(0) { intentFlags, flag ->
            intentFlags or flag
        }
        intent.putExtras(extras)
        intent.data = data
        intent.action = action
        customizer?.customize(intent)

        return intent
    }

    fun start(destination: KClass<*>) {
        start(destination.java)
    }

    fun start(destination: Class<*>) {
        val intent = prepareIntent(destination)
        if (Activity::class.java.isAssignableFrom(destination)) {
            startActivity(intent)
        } else if (Service::class.java.isAssignableFrom(destination)) {
            startService(intent)
        }
    }

    fun startActivity(intent: Intent) {
        resultCallback?.also { callback ->
            val receiver = object : ResultReceiver(Handler(Looper.getMainLooper())) {
                override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
                    callback.invoke(resultCode, resultData.getParcelable<Parcelable>("result") as? Intent)
                }
            }
            Navigator(if (isActivity) context as Activity else context)
                    .putParcelable("intent", intent)
                    .putParcelable("callback", receiver)
                    .start(InterceptorActivity::class.java)
        } ?: context.startActivity(intent)
    }

    fun startService(intent: Intent) {
        context.startService(intent)
    }

    fun bindTo(destination: Class<*>, connection: ServiceConnection) {
        this.bindTo(destination, connection, Context.BIND_AUTO_CREATE)
    }

    fun bindTo(destination: Class<*>, connection: ServiceConnection, flags: Int) {
        context.bindService(prepareIntent(destination), connection, flags)
    }
}
