package papyrus.adapter.sorted

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.SortedList
import papyrus.adapter.*
import papyrus.util.PapyrusExecutor

abstract class SortedDataSource(vararg modules: Module) : DataSource(), ModuleObserver {
    var moduleRegistry = ModuleRegistry(arrayListOf(*modules))
    val data: SortedList<DataItem<*>> = SortedList(DataItem::class.java, DataSourceSorter(adapter))
    var paginationThreshold: Int? = null
    var dataEnded: Boolean = true

    open val singlePage: Boolean = false

    override fun count() = data.size()
    override fun getItem(position: Int): DataItem<*> {
        paginationThreshold?.let { threshold ->
            if (!dataEnded && !loading && position > count() - threshold) {
                load()
            }
        }

        return data[position]
    }

    fun enablePagination(threshold: Int) {
        paginationThreshold = threshold
        dataEnded = false
    }

    @CallSuper
    open fun refresh() {
        data.clear()
        moduleRegistry.refresh()
        paginationThreshold?.let {
            dataEnded = false
        }
        load()
    }

    private fun addItem(index: Int, thing: Any): Int = moduleRegistry.modulesForIndex(index)
        .fold(ArrayList<DataItem<*>>()) { items, module ->
            module.createDataItem(index + items.size, this)?.let(items::add)
            items
        }.takeIf {
            it.isNotEmpty()
        }?.let { items ->
            items.forEach { data.add(it) }
            addItem(index + items.size, thing)
        }
        ?: run {
            data.add(createDefaultDataItem(index, thing))
            index + 1
        }

    override fun update(newData: ArrayList<out Any>) {
        submit(newData, false)
    }

    fun submit(newData: ArrayList<out Any>, clear: Boolean) {
        PapyrusExecutor.ui {
            data.beginBatchedUpdates()
            if (clear) data.clear()

            moduleRegistry.eagerModules
                .mapNotNull { it.createDataItem(it.target, this) }
                .forEach { data.add(it) }
            val insertPoint = data.size().takeIf { !singlePage } ?: 0
            newData.takeIf { it.isNotEmpty() }?.fold(insertPoint, ::addItem)
            data.endBatchedUpdates()
            loading = false
        }
    }

    override fun onChanged(item: DataItem<*>) {
        if (data.size() > item.target) {
            data.updateItemAt(item.target, item)
        }
    }
}
