package papyrus.picker.adapter

import android.view.ViewGroup
import android.widget.TextView
import papyrus.adapter.DataItem
import papyrus.adapter.PapyrusViewHolder
import papyrus.picker.R

class OptionViewHolder<T>(parent: ViewGroup) : PapyrusViewHolder<DataItem<T>>(parent, R.layout.picker_option_default) {

    val labelOption = itemView.findViewById<TextView>(R.id.label_option)

    override fun bind(dataItem: DataItem<T>) {
        labelOption.text = dataItem.item.toString()
    }

}