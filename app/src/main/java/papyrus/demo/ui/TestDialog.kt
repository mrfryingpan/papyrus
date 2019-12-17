package papyrus.demo.ui

import android.widget.TextView
import papyrus.alerts.ViewBinder
import papyrus.demo.R

class TestDialog : ViewBinder() {

    override fun buttonIDs() = arrayOf(
            R.id.button_positive,
            R.id.button_negative
    )

    override fun bind() {
        itemView.findViewById<TextView>(R.id.label_title)?.apply {
            text = "Test Dialog"
        }
        itemView.findViewById<TextView>(R.id.label_message)?.apply {
            text = "This Dialog Tests the new Dialog Activity Functionality"
        }
        itemView.findViewById<TextView>(R.id.button_positive)?.apply {
            text = "OK"
        }
        itemView.findViewById<TextView>(R.id.button_negative)?.apply {
            text = "That's Cool"
        }

    }
}