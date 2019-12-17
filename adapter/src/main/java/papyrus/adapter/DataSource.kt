package papyrus.adapter

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.SortedList
import android.view.ViewGroup
import papyrus.util.PapyrusExecutor

abstract class DataSource<T : DataItem>(vararg modules: Module) {
    var moduleRegistry = ModuleRegistry(arrayListOf(*modules))
    val data: SortedList<DataItem> = SortedList(DataItem::class.java, DataSourceSorter { adapter })
    var paginationThreshold: Int? = null
    var dataEnded: Boolean = true
    var loading: Boolean = false
    var adapter: DataSourceAdapter? = null

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
        moduleRegistry.refresh()
        paginationThreshold?.let {
            dataEnded = false
        }
        loadNext()
    }

    private fun addItem(index: Int, thing: Any): Int {
        val moduleItems = moduleRegistry.modulesForIndex(index)?.fold(ArrayList<DataItem?>()) { items, module ->
            module.load(index + items.size)?.let(items::add)
            items
        }
        return moduleItems?.let { items ->
            if (items.isEmpty()) {
                null
            } else {
                items.forEach { data.add(it) }
                addItem(index + items.size, thing)
                index + items.size
            }
        } ?: run {
            data.add(createDefaultDataItem(index, thing))
            index + 1
        }
    }

    @CallSuper
    open fun loadNext() {
        loading = true
        makeNextRequest { page ->
            loading = false
            PapyrusExecutor.ui {
                data.beginBatchedUpdates()
                if (page.isNullOrEmpty()) {
                    dataEnded = true
                } else {
                    page.fold(data.size()) { index, item ->
                        addItem(index, item)
                    }
                }
                data.endBatchedUpdates()
            }
        }
    }

    abstract fun createViewHolder(parent: ViewGroup, viewType: Int): PapyrusViewHolder<out DataItem>
    protected abstract fun createDefaultDataItem(index: Int, data: Any): DataItem
    protected abstract fun makeNextRequest(onNewPage: (ArrayList<out Any>) -> Unit)
}
