package papyrus.adapter

import androidx.annotation.CallSuper

abstract class RepeatingModule(val start: Int, val frequency: Int, val max: Int = Int.MAX_VALUE) : Module(){
    var placementCount = 0

    @CallSuper
    override fun invalidate() {
        placementCount = 0
    }
}