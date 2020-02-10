package papyrus.demo.ui

import android.os.Bundle
import android.widget.TextView
import papyrus.alerts.ViewBinder
import papyrus.demo.R

class TestDialog : ViewBinder() {
    val title: TextView = itemView.findViewById(R.id.label_title)
    val message: TextView = itemView.findViewById(R.id.label_message)
    val positive: TextView = itemView.findViewById(R.id.button_positive)
    val negative: TextView = itemView.findViewById(R.id.button_negative)

    override fun bind(config: Bundle?) {
        title.text = config?.getString("title")
        message.text = config?.getString("message")
        positive.text = config?.getString("positive")
        negative.text = config?.getString("negative")
    }
}