package papyrus.alerts

import android.view.LayoutInflater
import android.view.ViewGroup


abstract class ViewBinder {
    lateinit var itemView: ViewGroup

    fun initializeView(parent: ViewGroup): ViewGroup {
        itemView = (LayoutInflater.from(parent.context).inflate(layoutID, parent, false) as? ViewGroup)
                ?: throw IllegalStateException("layout ${parent.context.resources.getResourceName(layoutID)} must have ViewGroup root")
        parent.addView(itemView)
        return itemView
    }

    open val layoutID: Int = R.layout.dialog_default

    abstract fun buttonIDs(): Array<Int>

    abstract fun bind()
}

