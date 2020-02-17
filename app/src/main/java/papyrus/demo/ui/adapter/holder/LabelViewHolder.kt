package papyrus.demo.ui.adapter.holder

import android.view.ViewGroup
import android.widget.TextView
import papyrus.adapter.PapyrusViewHolder
import papyrus.demo.R
import papyrus.demo.ui.adapter.item.LabelItem

class LabelViewHolder(parent: ViewGroup) : PapyrusViewHolder<LabelItem>(parent, R.layout.item_button) {
    val label = itemView.findViewById<TextView>(R.id.label)

    override fun bind(dataItem: LabelItem) {
        label.text = dataItem.name
        label.setOnClickListener { dataItem.onClick?.invoke() }
    }
}