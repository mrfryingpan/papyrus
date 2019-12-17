package papyrus.fonts

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

open class PapyrusTypefaceSpan(context: Context, font: String) : TypefaceSpan("") {

    private val newType: Typeface = TypefaceLoader.getTypeface(font, context)

    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, newType)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, newType)
    }

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
        paint.typeface = tf
    }
}
