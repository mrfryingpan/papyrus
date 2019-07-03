package papyrus.adapter

abstract class SimpleModule(val target: Int) : Module {
    override fun wantsPlacement(index: Int): Boolean {
        return target == index
    }
}