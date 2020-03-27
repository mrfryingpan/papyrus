package papyrus.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class PapyrusViewHolder<T : DataItem<*>>(itemView: View)
    : RecyclerView.ViewHolder(itemView) {

    constructor(parent: ViewGroup, layoutID: Int) : this(LayoutInflater.from(parent.context).inflate(layoutID, parent, false))

    internal lateinit var data: T


    @Suppress("UNCHECKED_CAST")
    internal fun doBind(_dataItem: DataItem<*>) {
        (_dataItem as? T)?.let {
            data = it
            bind(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    internal fun doBind(_dataItem: DataItem<*>, payloads: List<Any>) {
        (_dataItem as? T)?.let {
            data = it
            bind(it, payloads)
        }
    }

    abstract fun bind(dataItem: T)
    open fun bind(dataItem: T, payloads: List<Any>) = bind(dataItem)

}