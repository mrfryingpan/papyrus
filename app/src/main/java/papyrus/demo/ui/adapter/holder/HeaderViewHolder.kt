package papyrus.demo.ui.adapter.holder

import android.view.ViewGroup
import android.widget.TextView
import papyrus.adapter.PapyrusViewHolder
import papyrus.demo.R
import papyrus.demo.ui.adapter.item.HeaderItem

class HeaderViewHolder(parent: ViewGroup): PapyrusViewHolder<HeaderItem>(parent, R.layout.item_header) {
    val label = itemView.findViewById<TextView>(R.id.label)

    override fun bind(dataItem: HeaderItem) {
        label.text = "Loaded ${dataItem.item} Times"
    }
}