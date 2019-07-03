package papyrus.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup


class DataSourceAdapter<T : DataItem>(private val dataSource: DataSource<T>)
    : RecyclerView.Adapter<PapyrusViewHolder<T>>() {

    init {
        dataSource.adapter = this
    }

    override fun getItemViewType(position: Int): Int {
        return dataSource.getItem(position).type
    }

    override fun getItemCount(): Int {
        return dataSource.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PapyrusViewHolder<T> {
        return dataSource.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: PapyrusViewHolder<T>, position: Int) {
        holder.doBind(dataSource.getItem(position))
    }
}

