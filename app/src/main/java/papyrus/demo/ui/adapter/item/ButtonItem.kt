package papyrus.demo.ui.adapter.item

import papyrus.adapter.DataItem
import papyrus.demo.ui.adapter.DataTypes

class ButtonItem(target: Int, val name: String, val onClick: () -> Unit) : DataItem(target, name, DataTypes.BUTTON.ordinal)