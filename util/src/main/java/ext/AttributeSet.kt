package ext

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet


fun AttributeSet.use(
        context: Context,
        styleable: IntArray,
        defStyle: Int = 0,
        action: TypedArray.() -> Unit
) {
    val a = context.obtainStyledAttributes(this, styleable, defStyle, 0)
    a.apply(action)
    a.recycle()
}