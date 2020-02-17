package papyrus.demo.data.vm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import papyrus.demo.data.live.LiveDataRegistry
import papyrus.demo.data.model.PickerModel

class PickerViewModel : ViewModel() {

    val data = LiveDataRegistry.picker

    fun invalidate() {
        data.invalidate()
    }

    fun observe(owner: LifecycleOwner, observer: Observer<PickerModel>) {
        data.observe(owner, observer)
    }
}