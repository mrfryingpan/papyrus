package papyrus.core.iface

import android.content.Intent

interface IResultCallback {

    fun onResult(resultCode: Int, data: Intent)
}
