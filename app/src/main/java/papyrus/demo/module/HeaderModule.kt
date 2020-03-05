package papyrus.demo.module

import papyrus.adapter.DataItem
import papyrus.adapter.EagerModule
import papyrus.demo.ui.adapter.item.HeaderItem

class HeaderModule : EagerModule(0) {
    var item: HeaderItem? = null
    private var loadCount = 0

    override fun invalidate() {
        loadCount++
        item?.item = loadCount
    }

    override fun createDataItem(index: Int): DataItem<*>? = item
            ?: HeaderItem(index, loadCount).also {
                item = it
            }
}