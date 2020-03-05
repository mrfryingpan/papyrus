package papyrus.demo.module

import papyrus.adapter.EagerModule
import papyrus.adapter.SimpleModule
import papyrus.demo.ui.adapter.item.ButtonItem

class ButtonModule(placement: Int, val name: String, val onClick: () -> Unit) : SimpleModule(placement) {
    override fun invalidate() {
        // No Refresh
    }

    override fun createDataItem(index: Int): ButtonItem? {
        return ButtonItem(index, name, onClick)
    }

}
