package papyrus.spanner

import android.os.Bundle
import android.os.Parcel
import android.os.ResultReceiver
import android.text.style.ClickableSpan
import android.view.View
import java.io.Serializable

class OnClickSpan(val clickCallback: () -> Unit) : ClickableSpan() {

    override fun onClick(widget: View) {
        clickCallback()
    }
}