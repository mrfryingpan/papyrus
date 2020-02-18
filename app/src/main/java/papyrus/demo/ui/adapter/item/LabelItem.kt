package papyrus.demo.ui.adapter.item

import papyrus.adapter.DataItem
import papyrus.demo.ui.adapter.DataTypes

class LabelItem(target: Int, val name: String?, val onClick: (() -> Unit)? = null) : DataItem<String>(target, name, DataTypes.LABEL.ordinal){
    override fun calculateChange(newItem: DataItem<String>): Any? = "TEST"
}
