package papyrus.demo.module

import papyrus.adapter.DataItem
import papyrus.adapter.ModuleObserver
import papyrus.adapter.RepeatingModule
import papyrus.demo.ui.adapter.item.LabelItem
import java.util.*

class AlphaModule(start: Int, frequency: Int) : RepeatingModule(start, frequency, 5) {
    var stuff = IntRange(0, 25).fold(LinkedList<Char>()) { acc, int ->
        acc.apply { this.add('A' + int) }
    }

    override fun invalidate() {

    }

    override fun createDataItem(index: Int): LabelItem? {
        return stuff.poll()?.let { LabelItem(index, it.toString()) }
    }
}