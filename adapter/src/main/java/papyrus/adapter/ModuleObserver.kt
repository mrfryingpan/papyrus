package papyrus.adapter

interface ModuleObserver {
    fun onChanged(item: DataItem<*>)
}