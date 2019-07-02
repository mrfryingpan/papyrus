package papyrus.sleuth.ga

object Params {
    const val EVENT_TYPE = "&t"
    const val SCREEN_NAME = "SCREEN_NAME"
    const val CATEGORY = "&ec"
    const val ACTION = "&ea"
    const val LABEL = "&el"
    const val VALUE = "&ev"

    fun customDimension(index: Int): String = "&cd$index"

    fun customMetric(index: Int): String = "&cm$index"
}