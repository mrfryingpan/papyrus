package papyrus.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class ItemDecoration : RecyclerView.ItemDecoration() {
    @Suppress("UNCHECKED_CAST")
    protected fun holderAt(index: Int, parent: RecyclerView) =
            parent.getChildAt(index)?.let { childView ->
                parent.getChildViewHolder(childView)
            }

    protected fun holderAtAdapterPosition(index: Int, parent: RecyclerView) =
            (parent.layoutManager as? LinearLayoutManager)
                    ?.findViewByPosition(index)
                    ?.let { view ->
                        parent.getChildViewHolder(view)
                    }

    protected fun firstVisibleHolder(parent: RecyclerView): RecyclerView.ViewHolder? =
            (parent.layoutManager as? LinearLayoutManager)
                    ?.let { it.findViewByPosition(it.findFirstVisibleItemPosition()) }
                    ?.let(parent::getChildViewHolder)
}