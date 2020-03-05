package papyrus.demo.ui.adapter.item

import papyrus.adapter.DataItem
import papyrus.demo.ui.adapter.DataTypes

class HeaderItem(target: Int, loadCount: Int) : DataItem<Int>(target, loadCount, DataTypes.HEADER.ordinal)