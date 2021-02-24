package papyrus.adapter

import android.view.ViewGroup
import papyrus.util.PapyrusExecutor

abstract class DataSource {
    var adapter: DataSourceAdapter = DataSourceAdapter(this)
    var loading = false

    init {
        PapyrusExecutor.background(10) {
            load()
        }
    }

    open fun load() {
        loading = true
        makeNextRequest()
    }

    abstract fun getItem(position: Int): DataItem<*>
    abstract fun count(): Int

    abstract fun update(newData: ArrayList<out Any>)
    abstract fun createViewHolder(parent: ViewGroup, viewType: Int): PapyrusViewHolder<out DataItem<*>>
    protected abstract fun createDefaultDataItem(index: Int, data: Any): DataItem<*>
    protected abstract fun makeNextRequest()
}