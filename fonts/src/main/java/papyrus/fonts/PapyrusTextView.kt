package papyrus.fonts

import android.content.Context
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

open class PapyrusTextView : AppCompatTextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.PapyrusTextView,
                0, 0)

        try {
            a.getString(R.styleable.PapyrusTextView_papyrus_font)?.let { font ->
                if (font.isNotEmpty()) {
                    setFont(font)
                }
            }
        } finally {
            a.recycle()
        }
    }

    fun setFont(font: String) {
        typeface = TypefaceLoader.getTypeface(font, context)
    }
}
