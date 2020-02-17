package papyrus.picker.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import papyrus.picker.R
import papyrus.picker.adapter.PapyrusOptionAdapter

class PapyrusOptionField @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) : OptionField<String>(context, attributeSet) {

    lateinit var fieldLabel: TextView
    lateinit var fieldCaret: ImageView

    override val fieldLayoutID: Int
        get() = R.layout.picker_field_default
    override val pickerLayoutID: Int
        get() = R.layout.picker_drawer_default


    override fun bindField(fieldView: ViewGroup) {
        fieldLabel = fieldView.findViewById(R.id.label_selected)
        fieldCaret = fieldView.findViewById(R.id.image_caret)
    }

    override fun bindPicker(pickerView: ViewGroup) {
        optionRecycler = pickerView.findViewById(R.id.recycler_options)
        optionRecycler.layoutManager = LinearLayoutManager(pickerView.context)
    }

    override fun onItemSelected(value: String?) {
        fieldLabel.text = value
    }

    override fun onOptionsChanged(value: MutableList<String>?) {
        value?.let { options ->
            optionRecycler.adapter = PapyrusOptionAdapter(options)
        }
    }


}