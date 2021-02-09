package papyrus.spanner

import android.os.Parcel
import android.os.Parcelable
import android.text.Spannable
import android.text.SpannableString
import papyrus.core.navigation.readArbitrary
import papyrus.core.navigation.writeArbitrary
import java.util.*

@Suppress("UNCHECKED_CAST")
class Spanner() : Parcelable {
    private var stringBuilder = StringBuilder()
    private var spanRegistry = ArrayList<SpanDeclaration>()
    private var spanStack = Stack<SpanDeclaration>()

    constructor(parcel: Parcel) : this() {
        stringBuilder = parcel.readSerializable() as StringBuilder
        spanRegistry = parcel.readArbitrary()
    }

    fun beginSpan(obj: Any): Spanner {
        spanStack.push(SpanDeclaration(obj, stringBuilder.length, stringBuilder.length))
        return this
    }

    fun appendSpan(text: String, vararg spans: Any): Spanner {
        spans.forEach { obj ->
            spanRegistry.add(SpanDeclaration(obj, stringBuilder.length, stringBuilder.length + text.length))
        }
        stringBuilder.append(text)
        return this
    }

    fun endSpan(): Spanner {
        if (spanStack.isNotEmpty()) {
            spanStack.pop()?.apply {
                end = stringBuilder.length
            }?.let(spanRegistry::add)
        }
        return this
    }

    fun build(): Spannable = SpannableString(stringBuilder.toString()).apply {
        spanRegistry.forEach { span ->
            span.what?.let { obj ->
                setSpan(obj, span.start, span.end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(stringBuilder)
        parcel.writeArbitrary(spanRegistry)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Spanner> {
        override fun createFromParcel(parcel: Parcel): Spanner {
            return Spanner(parcel)
        }

        override fun newArray(size: Int): Array<Spanner?> {
            return arrayOfNulls(size)
        }
    }
}

data class SpanDeclaration(var what: Any?, var start: Int, var end: Int)
