package papyrus.adapter

import android.graphics.Canvas
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import delegate.WeakDelegate
import papyrus.util.PapyrusExecutor
import papyrus.util.Res
import kotlin.math.abs

@Suppress("UNCHECKED_CAST")
class StickyHeaderDecoration(frame: ViewGroup) : ItemDecoration() {

    val frame: ViewGroup? by WeakDelegate(frame)
    val headers = SparseArray<StickyDataItem<*>>()
    val headerHolders: SparseArray<StickyViewHolder<StickyDataItem<*>>> = SparseArray()

    var headerViewHolder: StickyViewHolder<StickyDataItem<*>>? = null
    var headerVisible: Boolean = false
        set(value) {
            field = value
            headerViewHolder?.itemView?.visibility = if (value) View.VISIBLE else View.GONE
        }

    fun remove(key: Int) = headers.remove(key)
    fun put(key: Int, item: StickyDataItem<*>, creator: (ViewGroup) -> StickyViewHolder<StickyDataItem<*>>?) {
        headers.put(key, item)
        headerHolders.get(item.type)
                ?: frame?.let(creator)?.also { headerHolders.put(item.type, it) }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val firstHolder = holderAt(0, parent)?.also { holder ->
            holder.adapterPosition.let { position ->
                var index = position
                while (headers.get(index) == null && index >= 0) {
                    index--
                }
                headers.get(index)
            }?.also { item ->
                headerViewHolder = headerHolders.get(item.type)?.apply {
                    bind(item)
                    itemView.takeIf { it.parent == null }?.let { frame?.addView(it) }
                    headerVisible = true
                }
            } ?: PapyrusExecutor.ui {
                headerVisible = false
                frame?.apply {
                    headerViewHolder?.let {
                        removeView(it.itemView)
                    }
                    invalidate()
                }
                headerViewHolder = null
            }
        }

        var index = 1
        var nextHeader: RecyclerView.ViewHolder?
        do {
            nextHeader = holderAt(index++, parent)
        } while (nextHeader != null && !StickyViewHolder::class.java.isAssignableFrom(nextHeader.javaClass))

        (nextHeader as? StickyViewHolder<StickyDataItem<*>>)?.let { nextHeaderViewHolder ->
            nextHeaderViewHolder.itemView.let { nextHeaderItemView ->
                headerViewHolder?.apply {
                    itemView.translationY = nextHeaderItemView.y
                            .minus(itemView.height)
                            .plus(Res.dp(5f))
                            .coerceAtMost(0f)
                            .also {
                                shadow?.alpha = 1 - abs(it) / itemView.height
                                nextHeaderViewHolder.shadow?.alpha = (abs(it) / itemView.height)
                            }
                }?: run {
                    nextHeaderViewHolder.shadow?.alpha = 0f
                }
            }
        } ?: run {
            headerViewHolder?.itemView?.translationY = 0f
            headerViewHolder?.shadow?.alpha = 1f
            (firstHolder as? StickyViewHolder<*>)?.shadow?.alpha = 0f
        }
    }
}