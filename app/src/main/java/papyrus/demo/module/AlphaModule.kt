package papyrus.demo.module

import papyrus.adapter.DataItem
import papyrus.adapter.RepeatingModule
import papyrus.demo.ui.adapter.item.LabelItem
import java.util.*

class AlphaModule(start: Int, frequency: Int) : RepeatingModule(start, frequency) {
    var stuff = IntRange(0, 25).fold(LinkedList<Char>()) { acc, int ->
        acc.apply { this.add('A' + int) }
    }

    override fun refresh() {

    }

    override fun load(index: Int): DataItem? {
        return stuff.poll()?.let { LabelItem(index, it.toString()) }
    }
}