package papyrus.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class StickyViewHolder<T : StickyDataItem<*>>(itemView: View)
    : PapyrusViewHolder<T>(itemView) {
    constructor(parent: ViewGroup, layoutID: Int) : this(LayoutInflater.from(parent.context).inflate(layoutID, parent, false));

    abstract val shadow: View?

}
