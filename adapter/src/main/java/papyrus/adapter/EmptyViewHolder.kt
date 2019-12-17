package papyrus.adapter

import android.view.ViewGroup

class EmptyViewHolder(parent: ViewGroup) : PapyrusViewHolder<DataItem>(parent, R.layout.item_empty) {
    override fun bind(dataItem: DataItem) {
        //This is empty. No Binding necessary
    }
}
