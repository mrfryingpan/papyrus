package papyrus.adapter

abstract class RepeatingModule(val start: Int, val frequency: Int, val priority: Int = -1) : Module()