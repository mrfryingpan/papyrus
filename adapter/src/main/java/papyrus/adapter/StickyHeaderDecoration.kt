package papyrus.adapter

import android.graphics.Canvas
import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import delegate.WeakDelegate
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@Suppress("UNCHECKED_CAST")
class StickyHeaderDecoration<T : StickyDataItem<out Any>, K : StickyViewHolder<T>>(
        val headerViewHolder: K,
        dataSource: DataSource<out DataItem<*>>
) : ItemDecoration() {

    val dataSource: DataSource<out DataItem<*>>? by WeakDelegate(dataSource)
    val headers = SparseArray<T>()

    private fun syncState(start: Int = 0, end: Int = (dataSource?.count() ?: 0)) {
        for (i in start..end) {
            headers.remove(i)
            dataSource?.data
                    ?.takeIf { it.size() > i }
                    ?.let { it[i] as? T }
                    ?.let {
                        headers.put(it.target, it)
                    }

        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val firstHolder = holderAt(0, parent)?.also {
            it.adapterPosition.let {
                var index = it
                while (headers.get(index) == null && index >= 0) {
                    index--
                }
                headers.get(index)
            }?.let {
                headerViewHolder.itemView.visibility = View.VISIBLE
                headerViewHolder.bind(it)
            } ?: run {
                headerViewHolder.itemView.visibility = View.GONE
            }
        }

        holderAt(1, parent)?.also { secondHolder ->
            when {
                secondHolder::class == headerViewHolder::class -> {
                    val secondItemView = secondHolder.itemView
                    headerViewHolder.apply {
                        itemView.translationY =
                                (secondItemView.y - headerViewHolder.itemView.height)
                                        .coerceAtMost(0f)
                                        .also {
                                            shadow?.alpha = 1 - abs(it) / itemView.height
                                            (secondHolder as? K)
                                                    ?.shadow?.alpha = (abs(it) / itemView.height)
                                        }
                    }
                    headerViewHolder.itemView.invalidate()
                }
                else -> {
                    headerViewHolder.itemView.translationY = 0f
                    headerViewHolder.shadow?.alpha = 1f
                    (firstHolder as? K)?.shadow?.alpha = 0f
                }
            }
        }
    }

    val adapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            syncState()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            syncState(positionStart, positionStart + itemCount)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            syncState(positionStart)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            syncState(positionStart)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            syncState(min(fromPosition, toPosition), max(fromPosition, toPosition) + itemCount)
        }
    }
}