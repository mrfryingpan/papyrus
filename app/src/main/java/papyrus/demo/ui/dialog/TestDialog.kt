package papyrus.demo.ui.dialog

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import papyrus.alerts.ViewBinder
import papyrus.demo.R
import papyrus.spanner.Spanner

class TestDialog : ViewBinder() {
    val title: TextView by lazy { itemView.findViewById(R.id.label_title) }
    val message: TextView by lazy { itemView.findViewById(R.id.label_message) }
    val positive: TextView by lazy { itemView.findViewById(R.id.button_positive) }
    val negative: TextView by lazy { itemView.findViewById(R.id.button_negative) }

    override fun bind(config: Bundle?) {
        title.text = config?.getString("title")
        message.text = config?.getParcelable<Spanner>("message")?.build()
        message.movementMethod = LinkMovementMethod()
        positive.text = config?.getString("positive")
        negative.text = config?.getString("negative")
    }
}