package papyrus.picker.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import ext.findParentWithID
import papyrus.picker.R
import papyrus.util.Animations

@Suppress("LeakingThis")
abstract class OptionField<T> @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) : FrameLayout(context, attributeSet) {

    private lateinit var contentRoot: ViewGroup
    private lateinit var sheetLayout: ViewGroup
    private lateinit var scrim: View
    private lateinit var drawer: FrameLayout
    private lateinit var pickerLayout: ViewGroup
    lateinit var optionRecycler: RecyclerView


    abstract val fieldLayoutID: Int
    abstract val pickerLayoutID: Int
    var selectedItem: T? = null
        set(value) {
            field = value
            if (attached)
                onItemSelected(value)
        }

    var options: MutableList<T>? = null
        set(value) {
            field = value
            if (attached)
                onOptionsChanged(value)
        }

    var attached = false

    val isOpen: Boolean
        get() = sheetLayout.parent != null


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        contentRoot = findParentWithID(android.R.id.content) ?: parent as ViewGroup
        val fieldLayout = LayoutInflater.from(context)
                .inflate(fieldLayoutID, this, true) as ViewGroup
        sheetLayout = LayoutInflater.from(context)
                .inflate(R.layout.view_picker_sheet, contentRoot, false) as ViewGroup
        scrim = sheetLayout.findViewById(R.id.scrim)
        drawer = sheetLayout.findViewById(R.id.drawer)
        pickerLayout = LayoutInflater.from(context).inflate(pickerLayoutID, drawer, true) as ViewGroup

        bindField(fieldLayout)
        bindPicker(pickerLayout)

        setOnClickListener { show() }
        scrim.setOnClickListener { hide() }
        attached = true
        selectedItem?.let { onItemSelected(it) }
        options?.let { onOptionsChanged(it) }
    }

    fun show() {
        if (!isOpen) {
            contentRoot.addView(sheetLayout)
            Animations.fadeIn(scrim)
            Animations.slideInBottom(drawer)
        }
    }

    fun hide() {
        Animations.slideOffBottom(drawer)
        Animations.fadeOut(scrim) {
            contentRoot.removeView(sheetLayout)
        }
    }

    abstract fun bindField(fieldView: ViewGroup)

    abstract fun bindPicker(pickerView: ViewGroup)

    abstract fun onItemSelected(value: T?)

    abstract fun onOptionsChanged(value: MutableList<T>?)

}