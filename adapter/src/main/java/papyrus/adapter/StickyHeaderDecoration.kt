package papyrus.adapter

import android.graphics.Canvas
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import delegate.WeakDelegate
import papyrus.util.PapyrusExecutor
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
    fun put(
        key: Int,
        item: StickyDataItem<*>,
        creator: (ViewGroup) -> StickyViewHolder<StickyDataItem<*>>?
    ) {
        headers.put(key, item)
        headerHolders.get(item.type)
            ?: frame
                ?.let(creator)
                ?.also { headerHolders.put(item.type, it) }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val firstHolder = holderAt(0, parent)
        Log.wtf("Sticky", "holderAtPosition: ${firstHolder?.adapterPosition}")
        firstHolder?.adapterPosition
            ?.downTo(0)
            ?.firstNotNullOfOrNull { headers.get(it) }
            ?.also { item ->
                headerViewHolder = headerHolders.get(item.type)
                    ?.apply {
                        bind(item)
                        itemView
                            .takeIf { it.parent == null }
                            ?.let { frame?.addView(it) }
                        headerVisible = true
                        Log.wtf("Sticky", this.toString())
                    }
            }
            ?: PapyrusExecutor.ui {
                headerVisible = false
                frame?.apply {
                    headerViewHolder?.itemView
                        ?.let(::removeView)
                    invalidate()
                }
                headerViewHolder = null
            }

        1.until(parent.childCount)
            .firstNotNullOfOrNull { holderAt(it, parent) as? StickyViewHolder<*> }
            ?.let { nextHeaderHolder ->
                Log.wtf("Sticky", nextHeaderHolder.toString())
                nextHeaderHolder.itemView.let { nextHeaderView ->
                    headerViewHolder?.apply {
                        itemView.translationY = nextHeaderView.y
                            .minus(contentHeight)
                            .coerceAtMost(0f)
                            .also {
                                shadow?.alpha = 1 - abs(it) / itemView.height
                                nextHeaderHolder.shadow?.alpha = (abs(it) / itemView.height)
                            }
                    } ?: run {
                        nextHeaderHolder.shadow?.alpha = 0f
                    }
                }
            }
            ?: run {
                headerViewHolder?.itemView?.translationY = 0f
                headerViewHolder?.shadow?.alpha = 1f
                (firstHolder as? StickyViewHolder<*>)?.shadow?.alpha = 0f
            }
    }
}