package papyrus.adapter.differ

import android.annotation.SuppressLint
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import papyrus.adapter.DataItem
import papyrus.adapter.DataSourceAdapter

class DataSourceDiffer<T : DataItem<*>>(val adapter: DataSourceAdapter) : DiffUtil.ItemCallback<T>() {
    val size: Int
        get() = differ.currentList.size
    val differ: AsyncListDiffer<T> = AsyncListDiffer(adapter, this)

    operator fun get(index: Int) = differ.currentList[index]

    fun clear() {
        differ.submitList(ArrayList())
    }

    fun set(data: ArrayList<T>) {
        differ.submitList(data)
    }

    fun replace(item: T) {
        differ.submitList(
                differ.currentList.let { list ->
                    list.firstOrNull { it.id == item.id }
                            ?.let { list.indexOf(it) }
                            ?.takeIf { it != -1 }
                            ?.let { index ->
                                ArrayList(list).apply {
                                    removeAt(index)
                                    add(index, item)
                                }
                            }
                }
        )
    }

    override fun areItemsTheSame(old: T, new: T): Boolean = old.id == new.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(old: T, new: T): Boolean =
            old.viewType == new.viewType && old.item == new.item
}