package papyrus.adapter

interface ModuleCallback {
    fun onComplete(item: DataItem, hasData: Boolean)
}
