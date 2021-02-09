package papyrus.core.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.os.ResultReceiver


class InterceptorActivity : Activity() {

    val wrappedIntent: Intent? by lazy { intent.getParcelableExtra<Intent>("intent") }
    val wrappedSender: IntentSender? by lazy { intent.getParcelableExtra<IntentSender>("intentSender") }
    val callback: ResultReceiver? by lazy { intent.getParcelableExtra<ResultReceiver>("callback") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        wrappedIntent?.let { chainIntent ->
            if ("requestPermissions" == chainIntent.action) {
                if (Build.VERSION.SDK_INT >= 23) {
                    chainIntent.getStringArrayListExtra("permissions")?.let { permissionsToRequest ->
                        requestPermissions(permissionsToRequest.toTypedArray(), 1)
                    }
                } else {
                    finish()
                }
            } else {
                startActivityForResult(chainIntent, 1)
            }
        }

        wrappedSender?.let { sender ->
            startIntentSenderForResult(sender, 2, null, 0, 0, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callback?.send(resultCode, Bundle().apply {
            putParcelable("result", data)
        })
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        callback?.send(RESULT_OK, Bundle().apply {
            putParcelable("result", Intent()
                    .putExtra("permissions", permissions)
                    .putExtra("grantResults", grantResults))
        })
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}
