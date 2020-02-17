package papyrus.demo.ui.adapter.item

import papyrus.adapter.DataItem
import papyrus.demo.ui.adapter.DataTypes

class PickerItem<T>(target: Int, model: T?) : DataItem<T>(target, model, DataTypes.PICKER.ordinal)