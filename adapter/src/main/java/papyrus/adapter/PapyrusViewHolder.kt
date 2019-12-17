package papyrus.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class PapyrusViewHolder<T : DataItem>(parent: ViewGroup, layoutID: Int)
    : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutID, parent, false)) {

    @Suppress("UNCHECKED_CAST")
    internal fun doBind(_dataItem: DataItem) {
        (_dataItem as? T)?.let(::bind)
    }

    abstract fun bind(dataItem: T)
}