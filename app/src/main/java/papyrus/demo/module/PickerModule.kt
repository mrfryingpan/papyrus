package papyrus.demo.module

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import papyrus.adapter.SimpleModule
import papyrus.demo.data.model.PickerModel
import papyrus.demo.data.vm.PickerViewModel
import papyrus.demo.ui.adapter.item.PickerItem

class PickerModule(placement: Int, owner: LifecycleOwner) : SimpleModule(placement) {
    private  var item: PickerItem<PickerModel>? = null
    private val viewModel = PickerViewModel()

    init {
        viewModel.observe(owner, Observer { data ->
            item?.item = data
        })
    }

    override fun invalidate() {
        item = null
        viewModel.invalidate()
    }

    override fun createDataItem(index: Int): PickerItem<PickerModel>? = PickerItem(index, viewModel.data.value).also {
        invalidate()
        item = it
    }
}