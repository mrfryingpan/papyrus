package papyrus.demo.ui.adapter.holder

import android.view.ViewGroup
import android.widget.TextView
import papyrus.adapter.PapyrusViewHolder
import papyrus.demo.R
import papyrus.demo.ui.adapter.item.ButtonItem

class ButtonViewHolder(parent: ViewGroup): PapyrusViewHolder<ButtonItem>(parent, R.layout.item_button) {
    val label = itemView.findViewById<TextView>(R.id.label)

    override fun bind(dataItem: ButtonItem) {
        label.text = dataItem.name
        label.setOnClickListener { dataItem.onClick() }
    }
}