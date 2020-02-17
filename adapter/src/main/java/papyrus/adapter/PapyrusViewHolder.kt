package papyrus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class PapyrusViewHolder<T : DataItem<*>>(parent: ViewGroup, layoutID: Int)
    : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutID, parent, false)) {

    internal lateinit var data: T

    @Suppress("UNCHECKED_CAST")
    internal fun doBind(_dataItem: DataItem<*>) {
        (_dataItem as? T)?.let {
            data = it
            bind(it)
        }
    }

    abstract fun bind(dataItem: T)
}