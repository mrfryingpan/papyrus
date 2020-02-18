package papyrus.adapter

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.SortedList
import delegate.WeakDelegate
import papyrus.util.PapyrusExecutor

abstract class DataSource<T : DataItem<*>>(vararg modules: Module) : ModuleObserver {
    var moduleRegistry = ModuleRegistry(arrayListOf(*modules))
    val data: SortedList<DataItem<*>> = SortedList(DataItem::class.java, DataSourceSorter { adapter })
    var paginationThreshold: Int? = null
    var dataEnded: Boolean = true
    var loading: Boolean = false
    var adapter: DataSourceAdapter? by WeakDelegate()

    open val singlePage: Boolean = false

    fun count() = data.size()

    fun getItem(position: Int): DataItem<*> {
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
    fun refresh(callback: () -> Unit) {
        data.clear()
        moduleRegistry.refresh()
        paginationThreshold?.let {
            dataEnded = false
        }
        loadNext(callback)
    }

    private fun addItem(index: Int, thing: Any): Int = moduleRegistry.modulesForIndex(index)
            ?.fold(ArrayList<DataItem<*>>()) { items, module ->
                module.createDataItem(index + items.size, this)
                        ?.let(items::add)
                items
            }?.takeIf {
                it.isNotEmpty()
            }?.let { items ->
                items.forEach { data.add(it) }
                addItem(index + items.size, thing)
            }
            ?: run {
                data.add(createDefaultDataItem(index, thing))
                index + 1
            }


    @CallSuper
    open fun loadNext(callback: (() -> Unit)? = null) {
        loading = true
        makeNextRequest { page ->
            loading = false
            PapyrusExecutor.ui {
                data.beginBatchedUpdates()
                if (page.isNullOrEmpty()) {
                    dataEnded = true
                } else {
                    page.fold(if (singlePage) 0 else data.size(), ::addItem)
                }
                data.endBatchedUpdates()
                callback?.invoke()
            }
        }
    }

    override fun onChanged(item: DataItem<*>) {
        data.updateItemAt(item.target, item)
    }

    abstract fun createViewHolder(parent: ViewGroup, viewType: Int): PapyrusViewHolder<out DataItem<*>>
    protected abstract fun createDefaultDataItem(index: Int, data: Any): DataItem<*>
    protected abstract fun makeNextRequest(onNewPage: (ArrayList<out Any>) -> Unit)
}
