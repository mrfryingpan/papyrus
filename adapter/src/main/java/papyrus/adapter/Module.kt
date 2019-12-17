package papyrus.adapter

interface Module {
    fun refresh()

    fun load(index: Int): DataItem?
}