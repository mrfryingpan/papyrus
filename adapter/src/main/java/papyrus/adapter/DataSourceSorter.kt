package papyrus.adapter

import androidx.recyclerview.widget.SortedList

class DataSourceSorter(val adapter: () -> DataSourceAdapter?) : SortedList.Callback<DataItem>() {
    override fun areItemsTheSame(a: DataItem?, b: DataItem?): Boolean {
        return a?.target == b?.target
    }

    override fun areContentsTheSame(a: DataItem?, b: DataItem?): Boolean {
        return a?.viewType == b?.viewType && a?.item == b?.item
    }

    override fun compare(a: DataItem, b: DataItem): Int {
        return a.target.compareTo(b.target)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter()?.notifyItemMoved(fromPosition, toPosition)
    }

    override fun onChanged(position: Int, count: Int) {
        adapter()?.notifyItemRangeChanged(position, count)
    }

    override fun onInserted(position: Int, count: Int) {
        adapter()?.notifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        adapter()?.notifyItemRangeRemoved(position, count)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        adapter()?.notifyItemRangeChanged(position, count, payload)
    }
}
