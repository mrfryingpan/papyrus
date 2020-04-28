package papyrus.adapter

abstract class Module {
    abstract fun invalidate()

    abstract fun createDataItem(index: Int): DataItem<*>?

    fun createDataItem(index: Int, observer: ModuleObserver): DataItem<*>? {
        return createDataItem(index)?.also {
            it.observer = observer
        }
    }
}