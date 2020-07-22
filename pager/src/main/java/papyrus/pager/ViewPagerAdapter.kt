package papyrus.pager

import android.database.DataSetObserver
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import papyrus.util.PapyrusUtil
import java.util.*

abstract class ViewPagerAdapter<VH : ViewPagerAdapter.ViewHolder> : PagerAdapter() {

    private val viewPool = SparseArray<LinkedList<VH>>()
    private val activeHolders = HashMap<Int, VH>()

    val dataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            activeHolders.forEach { (position, holder) ->
                onBindViewHolder(holder, position)
            }
        }
    }

    init {
        registerDataSetObserver(dataSetObserver)
    }

    private fun retrieveViewHolderOfTypeFromPool(viewType: Int): VH? {
        return if (PapyrusUtil.isEmpty(viewPool.get(viewType))) {
            null
        } else {
            viewPool.get(viewType).removeFirst()
        }
    }

    private fun stashViewHolderForReuse(holder: VH) {
        if (viewPool.get(holder.viewType) == null) {
            viewPool.put(holder.viewType, LinkedList())
        }
        viewPool.get(holder.viewType).push(holder)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewType = getItemViewType(position)
        val holder = retrieveViewHolderOfTypeFromPool(viewType)
                ?: onCreateViewHolder(container, viewType).also {
                    it.viewType = viewType
                }
        holder.adapterPosition = position
        activeHolders.put(position, holder)
        onBindViewHolder(holder, position)
        container.addView(holder.itemView)
        return holder
    }

    @Suppress("UNCHECKED_CAST")
    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        (item as? VH)?.let {
            activeHolders.remove(position)
            onViewRecycled(it)
            container.removeView(it.itemView)
            stashViewHolderForReuse(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun isViewFromObject(view: View, item: Any): Boolean {
        return (item as? VH)?.itemView === view
    }

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract fun onBindViewHolder(holder: VH, position: Int)

    open fun onViewRecycled(holder: VH) {}

    open fun getItemViewType(position: Int): Int {
        return 1
    }

    abstract class ViewHolder(var itemView: View) {
        internal var viewType = 1
        var adapterPosition: Int = 0
            internal set
    }
}

