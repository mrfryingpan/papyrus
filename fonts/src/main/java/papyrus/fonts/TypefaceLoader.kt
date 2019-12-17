package papyrus.fonts

import android.content.Context
import android.graphics.Typeface

import java.util.HashMap

object TypefaceLoader {
    private val typefaces = HashMap<String, Typeface>()

    @Synchronized
    fun getTypeface(name: String, context: Context): Typeface {
        return typefaces[name]
                ?: Typeface.createFromAsset(context.assets, String.format("font/%s", name)).also { font ->
                    typefaces[name] = font
                }
    }
}
