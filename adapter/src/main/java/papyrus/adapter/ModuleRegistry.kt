package papyrus.adapter

import android.util.SparseArray
import papyrus.util.PapyrusExecutor

class ModuleRegistry(private val modules: ArrayList<Module>?) {
    private val eagerPlacements = ArrayList<SimpleModule>()
    private val placements = SparseArray<ArrayList<Module>>()
    private val draftTriggers = SparseArray<(Int) -> Int>()

    init {
        modules?.filterIsInstance<SimpleModule>()?.forEach { module ->
            registerPlacement(module.target, module)
        }

        modules?.filterIsInstance<RepeatingModule>()?.forEach { module ->
            val draftFunction = draftPlacements(module.frequency, module)
            val trigger = draftFunction(module.start)
            draftTriggers.put(trigger, draftFunction)
        }
    }

    private fun registerPlacement(target: Int, module: Module) {
        (module as? EagerModule)?.let(eagerPlacements::add)
        placements.put(target, (placements.get(target) ?: ArrayList()).apply {
            add(module)
        })

    }

    private fun draftPlacements(frequency: Int, module: Module) = fun(start: Int): Int {
        var target = start
        var trigger = target
        repeat(9) {
            registerPlacement(target, module)
            trigger = target
            target += frequency
        }
        return trigger
    }

    private fun modulesAtIndex(index: Int): ArrayList<Module>? {
        PapyrusExecutor.background {
            draftTriggers[index]?.let {
                draftTriggers.remove(index)
                draftTriggers.put(it(index), it)
            }
        }
        return placements[index]
    }

    val eagerModules: List<SimpleModule> = eagerPlacements

    fun modulesForIndex(index: Int): List<Module>? {
        return modulesAtIndex(index)?.apply {
            var lookForward = 1
            while (lookForward < size) {
                modulesAtIndex(index + lookForward)?.forEach { newModule ->
                    add(newModule)
                }
                lookForward += 1
            }
        }
    }

    fun refresh() {
        modules?.forEach { it.invalidate() }
    }
}