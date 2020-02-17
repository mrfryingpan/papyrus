package papyrus.demo.ui.adapter.holder

import android.view.ViewGroup
import android.widget.TextView
import papyrus.adapter.PapyrusViewHolder
import papyrus.alerts.DialogBuilder
import papyrus.demo.R
import papyrus.demo.data.model.PickerModel
import papyrus.demo.data.vm.PickerViewModel
import papyrus.demo.ui.adapter.item.PickerItem
import papyrus.demo.ui.dialog.PickerDialog

class PickerViewHolder(parent: ViewGroup) : PapyrusViewHolder<PickerItem<PickerModel>>(parent, R.layout.item_picker) {
    val viewModel = PickerViewModel()
    val picker: TextView = itemView.findViewById(R.id.field_picker)

    override fun bind(dataItem: PickerItem<PickerModel>) {
        picker.text = dataItem.item?.selected ?: dataItem.item?.hint
        picker.setOnClickListener {
            DialogBuilder()
                    .hostLayout(R.layout.activity_bottom_sheet)
                    .viewBinder(PickerDialog::class)
                    .configuration {
                        dataItem.item?.selected?.let { selected -> putString("selected", selected) }
                        dataItem.item?.options?.let { options -> putStringArray("options", options) }
                    }
                    .callback {
                        viewModel.data.updateValue {
                            selected = dataItem.item?.options?.getOrNull(it)
                        }
                    }
                    .show()
        }
    }
}