package papyrus.adapter.differ

import androidx.annotation.CallSuper
import papyrus.adapter.*
import papyrus.util.PapyrusExecutor

abstract class DifferDataSource(vararg modules: Module) : DataSource(), ModuleObserver {
    var moduleRegistry = ModuleRegistry(arrayListOf(*modules))
    val data: DataSourceDiffer<DataItem<*>> = DataSourceDiffer(adapter)
    var paginationThreshold: Int? = null
    var dataEnded: Boolean = true

    override fun count() = data.size

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

    override fun update(newData: ArrayList<out Any>) {
        PapyrusExecutor.ui {
            data.set(newData.fold(ArrayList<DataItem<*>>()) { acc, item ->
                acc.apply {
                    moduleRegistry.modulesForIndex(size)
                        .onEach { module ->
                            module.createDataItem(size, this@DifferDataSource)
                                ?.let {
                                    add(it)
                                }
                        }
                    add(createDefaultDataItem(size, item))
                }
            }.apply {
                moduleRegistry.eagerModules
                    .filter { it.target >= size }
                    .onEach { module ->
                        module.createDataItem(size, this@DifferDataSource)
                            ?.let {
                                add(it)
                            }
                    }
            })
            loading = false
        }
    }

    override fun onChanged(item: DataItem<*>) {
        data.replace(item)
    }
}
