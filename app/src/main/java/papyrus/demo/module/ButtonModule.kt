package papyrus.demo.module

import papyrus.adapter.SimpleModule
import papyrus.demo.ui.adapter.item.ButtonItem

class ButtonModule(placement: Int, val name: String, val onClick: () -> Unit) : SimpleModule(placement) {
    override fun refresh() {
        // No Refresh
    }

    override fun load(index: Int): ButtonItem? {
        return ButtonItem(index, name, onClick)
    }

}
