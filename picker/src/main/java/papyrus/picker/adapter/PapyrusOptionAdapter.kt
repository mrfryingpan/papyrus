package papyrus.picker.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PapyrusOptionAdapter<T>(options: MutableList<T>) : RecyclerView.Adapter<OptionViewHolder<T>>() {
    val options = options.mapIndexed { index, option -> OptionItem(index, option) }
    override fun getItemCount(): Int = options.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder<T> =
            OptionViewHolder(parent)

    override fun onBindViewHolder(holder: OptionViewHolder<T>, position: Int) =
            holder.bind(options[position])
}