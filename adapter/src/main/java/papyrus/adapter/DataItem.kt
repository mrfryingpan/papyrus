package papyrus.adapter

abstract class DataItem(val target: Int, var item: Any?, val type: Int) {
    val viewType: Int
        get() = item?.let { type } ?: -1
}