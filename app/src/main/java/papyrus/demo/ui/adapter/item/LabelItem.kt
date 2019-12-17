package papyrus.demo.ui.adapter.item

import papyrus.adapter.DataItem
import papyrus.demo.ui.adapter.DataTypes

class LabelItem(target: Int, val name: String?) : DataItem(target, name, DataTypes.LABEL.ordinal)
