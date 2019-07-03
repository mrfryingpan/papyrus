package papyrus.adapter

abstract class RepeatingModule(val start: Int, val frequency: Int) : Module {

    override fun wantsPlacement(index: Int): Boolean {
        return index >= start && (index - start) % frequency == 0
    }
}