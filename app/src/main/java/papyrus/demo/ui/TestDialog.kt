package papyrus.demo.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.dialog_default.view.*
import papyrus.alerts.ViewBinder

class TestDialog : ViewBinder() {
    val title = itemView.label_title
    val message = itemView.label_message
    val positive = itemView.button_positive
    val negative = itemView.button_negative

    override fun bind(bundle: Bundle?) {
        title.text = bundle?.getString("title")
        message.text = bundle?.getString("message")
        positive.text = bundle?.getString("positive")
        negative.text = bundle?.getString("negative")
    }
}