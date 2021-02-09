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

@Suppress("UNCHECKED_CAST")
class DialogActivity : AppCompatActivity() {
    private val dialogRootLayout: ViewGroup by lazy { findViewById<ViewGroup>(R.id.dialog_root) }
    private val dialogContentLayout: ViewGroup by lazy { findViewById<ViewGroup>(R.id.dialog_content) }

    private var resultReceiver: ResultReceiver? = null
    private var viewBinder: ViewBinder? = null

    private fun parseExtras(extras: Bundle) {
        resultReceiver = extras.getParcelable("resultReceiver")
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
            this.setContentView(intent.getIntExtra("parentLayoutID", R.layout.activity_dialog))
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
            viewBinder.initializeView(dialogContentLayout) { cancelable ->
                if (cancelable) {
                    TouchBlocker.unBlock(dialogRootLayout)
                } else {
                    TouchBlocker.block(dialogRootLayout)
                }
            }.also { contentView ->
                viewBinder.bind(intent.getBundleExtra("configuration"), send)
                intent.getIntArrayExtra("clickTargets")?.forEach { buttonID ->
                    contentView.findViewById<View>(buttonID).setOnClickListener {
                        send(buttonID)
                    }
                }
            }
        }
    }

    private val send: (Int) -> Unit = {
        handleBackPress {
            resultReceiver?.send(it, viewBinder?.resolveExtras(Bundle()))
        }
    }

    fun onCancel() {
        intent?.getIntExtra("cancelID", -1)?.takeIf { it != -1 }?.let { cancelID ->
            send(cancelID)
        }
    }

    override fun onBackPressed() {
        onCancel()
    }

    fun handleBackPress(after: (() -> Unit)? = null) {
        Animations.fadeOut(dialogRootLayout) {
            after?.invoke()
            finish()
            overridePendingTransition(0, 0)
        }
    }
}

class DialogBuilder(val bundle: Bundle) {
    val dialogCallbacks: DialogCallbacks = DialogCallbacks()

    constructor() : this(Bundle())

    fun configuration(configurator: Bundle.() -> Unit): DialogBuilder {
        bundle.putBundle("configuration", Bundle().apply(configurator))
        return this
    }

    fun viewBinder(viewBinder: KClass<out ViewBinder>): DialogBuilder {
        bundle.putSerializable("viewBinder", viewBinder.java)
        return this
    }

    fun hostLayout(@LayoutRes layoutID: Int): DialogBuilder {
        bundle.putInt("parentLayoutID", layoutID)
        return this
    }

    fun callback(id: Int, type: Int = ACTION_GENERIC, action: (result: Bundle) -> Unit): DialogBuilder {
        dialogCallbacks.addCallback(id, type, action)
        return this
    }

    fun callback(action: (buttonID: Int, result: Bundle) -> Unit): DialogBuilder {
        dialogCallbacks.addFallback(action)
        return this
    }

    fun handle(handle: Handle): DialogBuilder {
        bundle.putParcelable("handle", handle)
        return this
    }

    fun show() {
        dialogCallbacks.cancelID?.let { bundle.putInt("cancelID", it) }
        bundle.putIntArray("clickTargets", dialogCallbacks.buttonIDs)
        bundle.putParcelable("resultReceiver", object : ResultReceiver(PapyrusExecutor.uiHandler) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
                dialogCallbacks.onResult(resultCode, resultData)
            }
        })
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