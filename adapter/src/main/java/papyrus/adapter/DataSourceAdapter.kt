package papyrus.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class DataSourceAdapter(private val dataSource: DataSource<out DataItem<*>>)
    : RecyclerView.Adapter<PapyrusViewHolder<out DataItem<*>>>() {

    init {
        dataSource.adapter = this
    }

    override fun getItemViewType(position: Int): Int {
        return dataSource.getItem(position).viewType
    }

    override fun getItemCount(): Int {
        return dataSource.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PapyrusViewHolder<out DataItem<*>> {
        return if (viewType == -1) {
            EmptyViewHolder(parent)
        } else {
            dataSource.createViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: PapyrusViewHolder<out DataItem<*>>, position: Int) {
        holder.doBind(dataSource.getItem(position))
    }

    override fun onBindViewHolder(holder: PapyrusViewHolder<out DataItem<*>>, position: Int, payloads: MutableList<Any>) {
        holder.doBind(dataSource.getItem(position), payloads)
    }
}

