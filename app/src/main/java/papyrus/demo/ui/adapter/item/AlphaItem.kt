package papyrus.demo.ui.adapter.item

import papyrus.adapter.DataItem
import papyrus.adapter.StickyDataItem
import papyrus.demo.ui.adapter.DataTypes

class AlphaItem(target: Int, val name: String?, val onClick: (() -> Unit)? = null) : StickyDataItem<String>(target, name, DataTypes.ALPHA.ordinal){
    override fun calculateChange(newItem: DataItem<String>): Any? = "TEST"
}
