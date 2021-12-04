package papyrus.demo.ui.adapter.holder

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import papyrus.adapter.StickyViewHolder
import papyrus.demo.R
import papyrus.demo.ui.adapter.item.AlphaItem
import papyrus.util.Res

class AlphaViewHolder(parent: ViewGroup) :
    StickyViewHolder<AlphaItem>(parent, R.layout.item_alpha) {
    val label = itemView.findViewById<TextView>(R.id.label)
    override val shadow: View? = itemView.findViewById(R.id.shadow)
    override val contentHeight: Int
        get() = itemView.height - Res.dpi(5)

    override fun bind(dataItem: AlphaItem) {
        label.text = dataItem.name
        label.setOnClickListener { dataItem.onClick?.invoke() }
    }


}