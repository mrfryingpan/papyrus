package papyrus.alerts

import android.os.*
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import papyrus.core.Papyrus
import papyrus.util.Animations
import papyrus.util.PapyrusExecutor
import papyrus.util.TouchBlocker
import java.lang.ref.WeakReference
import kotlin.reflect.KClass

class DialogActivity : AppCompatActivity() {
    private val dialogRootLayout: ViewGroup by lazy { findViewById<ViewGroup>(R.id.dialog_root) }
    private val dialogContentLayout: ViewGroup by lazy { findViewById<ViewGroup>(R.id.dialog_content) }

    private var resultReceiver: ResultReceiver? = null
    private var viewBinder: ViewBinder? = null

    var cancelable = true
        set(value) {
            field = value
            if (value) {
                TouchBlocker.unBlock(dialogRootLayout)
            } else {
                TouchBlocker.block(dialogRootLayout)
            }
        }

    private fun parseExtras(extras: Bundle) {
        resultReceiver = extras.getParcelable("resultReceiver")
        @Suppress("UNCHECKED_CAST")
        viewBinder = (extras.getSerializable("viewBinder") as Class<ViewBinder>).newInstance()
        extras.getParcelable<ResultReceiver>("handle")?.let { instanceReceiver ->
            instanceReceiver.send(0, Bundle().apply {
                putParcelable("handle", InternalHandle(this@DialogActivity))
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.intent.extras?.also { extras ->
            parseExtras(extras)
            this.setContentView(R.layout.activity_dialog)
            overridePendingTransition(0, 0)
            configureLayout()
            this.dialogRootLayout.setOnClickListener {
                onCancel()
            }
            Animations.fadeIn(this.dialogRootLayout)
        } ?: finish()

    }

    private fun configureLayout() {
        viewBinder?.let { viewBinder ->
            viewBinder.initializeView(dialogContentLayout).also { contentView ->
                viewBinder.bind(intent.getBundleExtra("configuration"))
                viewBinder.buttonIDs().forEach { buttonID ->
                    contentView.findViewById<View>(buttonID).setOnClickListener {
                        resultReceiver?.send(buttonID, Bundle())
                        handleBackPress()
                    }
                }
            }
        }

    }

    fun onCancel() {
        intent?.getIntExtra("cancelID", -1)?.takeIf { it != -1 }?.let { cancelID ->
            resultReceiver?.send(cancelID, Bundle())
        }
        handleBackPress()
    }

    override fun onBackPressed() {
        onCancel()
    }

    fun handleBackPress() {
        Animations.fadeOut(dialogRootLayout) {
            finish()
            overridePendingTransition(0, 0)
        }
    }
}

class DialogBuilder(val bundle: Bundle) {
    constructor() : this(Bundle())

    fun configuration(configurator: Bundle.() -> Unit): DialogBuilder {
        bundle.putBundle("configuration", Bundle().apply(configurator))
        return this
    }

    fun viewBinder(viewBinder: KClass<out ViewBinder>): DialogBuilder {
        bundle.putSerializable("viewBinder", viewBinder.java)
        return this
    }

    fun layout(@LayoutRes layoutID: Int): DialogBuilder {
        bundle.putInt("layoutID", layoutID)
        return this
    }

    fun callbacks(vararg callbacks: DialogCallback): DialogBuilder {
        val dialogCallbacks = DialogCallbacks(*callbacks)
        dialogCallbacks.cancelID?.let { bundle.putInt("cancelID", it) }
        bundle.putParcelable("resultReceiver", object : ResultReceiver(PapyrusExecutor.uiHandler) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
                dialogCallbacks.onResult(resultCode)
            }
        })
        return this
    }

    fun handle(handle: Handle): DialogBuilder {
        bundle.putParcelable("handle", handle)
        return this
    }

    fun show() {
        Papyrus.navigate()
                .putAll(bundle)
                .start(DialogActivity::class)
    }
}

private class InternalHandle(instance: DialogActivity) : ResultReceiver(Handler(Looper.getMainLooper())) {
    private val instanceRef: WeakReference<DialogActivity> = WeakReference(instance)

    override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
        instanceRef.get()?.let {
            if (resultCode == ACTION_CANCEL) {
                it.onCancel()
            }
        }
    }
}


class Handle : ResultReceiver(Handler()) {
    var handle: ResultReceiver? = null

    override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
        handle = resultData.getParcelable<Parcelable>("handle") as ResultReceiver
    }

    fun dismiss() {
        handle?.send(ACTION_CANCEL, Bundle())
    }
}