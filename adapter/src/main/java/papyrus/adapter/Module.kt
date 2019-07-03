package papyrus.adapter

interface Module {
    fun refresh()

    fun wantsPlacement(index: Int): Boolean

    fun load(index: Int, callback: ModuleCallback)
}