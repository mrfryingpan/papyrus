package papyrus.adapter

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

class PapyrusRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
        FrameLayout(context, attrs) {

    val recycler: RecyclerView = RecyclerView(context)
    var layoutManager: RecyclerView.LayoutManager?
        get() = recycler.layoutManager
        set(value) {
            recycler.layoutManager = value
        }

    val stickyHeaders = StickyHeaderDecoration(this)

    var dataSource: DataSource? = null
        set(value) {
            field = value
            recycler.adapter = value?.adapter?.also {
                it.registerAdapterDataObserver(adapterDataObserver)
            }
            syncState()
        }

    init {
        addView(recycler, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        recycler.addItemDecoration(stickyHeaders)
    }

    @Suppress("UNCHECKED_CAST")
    private fun syncState(start: Int = 0, end: Int = (dataSource?.count() ?: 0)) {
        for (i in start..end) {
            stickyHeaders.remove(i)
            dataSource
                    ?.takeIf { it.count() > i }
                    ?.let { it.getItem(i) as? StickyDataItem }
                    ?.let { item ->
                        stickyHeaders.put(i, item) {
                            dataSource?.createViewHolder(this, item.viewType)
                                    as? StickyViewHolder<StickyDataItem<*>>
                        }
                    }

        }
    }

    val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
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