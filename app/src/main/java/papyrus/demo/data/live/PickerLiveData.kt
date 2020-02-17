package papyrus.demo.data.live

import androidx.lifecycle.MutableLiveData
import papyrus.demo.data.model.PickerModel
import papyrus.util.PapyrusExecutor


class PickerLiveData : MutableLiveData<PickerModel>() {

    fun invalidate() {
        val old = value
        value = null
        PapyrusExecutor.ui(500L) {
            value = PickerModel(old?.selected, old?.hint, arrayOf(
                    "Test 1",
                    "Test 2",
                    "Test 3",
                    "Test 4",
                    "Test 5",
                    "Test 6",
                    "Test 7",
                    "Test 8",
                    "Test 9"
            ))
        }
    }

    fun updateValue(apply: PickerModel.() -> Unit) {
        value = (value ?: PickerModel()).apply(apply)
    }
}