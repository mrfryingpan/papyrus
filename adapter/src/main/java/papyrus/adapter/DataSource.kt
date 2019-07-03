package papyrus.adapter

import android.support.annotation.CallSuper
import android.support.v7.util.SortedList
import android.view.ViewGroup
import papyrus.util.PapyrusExecutor
import papyrus.util.PapyrusUtil


abstract class DataSource<T : DataItem>(private val modules: List<Module>?) : SortedList.Callback<DataItem>() {
    var data: SortedList<DataItem> = SortedList<DataItem>(DataItem::class.java, this)
    var paginationThreshold: Int? = null
    var dataEnded: Boolean = true
    var loading: Boolean = false
    var adapter: DataSourceAdapter<T>? = null

    fun count() = data.size()

    fun getItem(position: Int): DataItem {
        paginationThreshold?.let { threshold ->
            if (!dataEnded && !loading && position > count() - threshold) {
                loadNext()
            }
        }

        return data[position]
    }

    fun enablePagination(threshold: Int) {
        paginationThreshold = threshold
        dataEnded = false
    }

    @CallSuper
    fun refresh() {
        data.clear()
        modules?.forEach { it.refresh() }
        paginationThreshold?.let {
            dataEnded = false
        }
        loadNext()
    }

    private fun addModulesAtIndex(index: Int): Int {
        modules?.forEach { module ->
            if (module.wantsPlacement(index)) {
                module.load(index, object : ModuleCallback {
                    override fun onComplete(item: DataItem, hasData: Boolean) {
                        if (hasData) {
                            PapyrusExecutor.ui {
                                data.add(item)
                            }
                        } else {
                            PapyrusExecutor.ui {
                                data.remove(item)
                            }
                        }
                    }
                })
                return addModulesAtIndex(index + 1) + 1
            }
        }
        return 0
    }

    fun onNewPage(page: List<Any>) {
        val start = data.size()

        PapyrusExecutor.ui {
            if (PapyrusUtil.isEmpty(page)) {
                dataEnded = true
            } else {
                page.forEachIndexed { i, thing ->
                    var index = start + i
                    index += addModulesAtIndex(index)
                    data.add(createDataItem(index, thing))
                }
            }
        }
    }


    @CallSuper
    open fun loadNext() {
        loading = true
    }

    abstract fun <T : DataItem> createViewHolder(parent: ViewGroup, viewType: Int): PapyrusViewHolder<T>
    protected abstract fun createDataItem(index: Int, data: Any): DataItem
    protected abstract fun makeNextRequest()

    override fun areItemsTheSame(a: DataItem?, b: DataItem?): Boolean {
        return a?.target == b?.target && a?.item == b?.item
    }

    override fun areContentsTheSame(a: DataItem?, b: DataItem?): Boolean {
        return a?.item == b?.item
    }

    override fun compare(a: DataItem, b: DataItem): Int {
        return a.target.compareTo(b.target)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter?.notifyItemMoved(fromPosition, toPosition)
    }

    override fun onChanged(position: Int, count: Int) {
        adapter?.notifyItemRangeChanged(position, count)
    }

    override fun onInserted(position: Int, count: Int) {
        adapter?.notifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        adapter?.notifyItemRangeRemoved(position, count)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        adapter?.notifyItemRangeChanged(position, count, payload)
    }
}
