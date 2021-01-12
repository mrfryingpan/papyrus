package papyrus.demo.module

import papyrus.adapter.RepeatingModule
import papyrus.demo.ui.adapter.item.AlphaItem
import java.util.*

class AlphaModule(start: Int, frequency: Int) : RepeatingModule(start, frequency, 5) {
    var stuff = IntRange(0, 25).fold(LinkedList<Char>()) { acc, int ->
        acc.apply { this.add('A' + int) }
    }

    override fun invalidate() {
        super.invalidate()
    }

    override fun createDataItem(index: Int): AlphaItem? {
        return stuff.poll()?.let { AlphaItem(index, it.toString()) }
    }
}