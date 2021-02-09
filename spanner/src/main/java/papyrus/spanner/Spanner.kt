package papyrus.spanner

import android.os.Parcel
import android.os.Parcelable
import android.text.ParcelableSpan
import android.text.Spannable
import android.text.SpannableString
import kotlinx.android.parcel.Parcelize
import java.util.*

class Spanner() : Parcelable {
    private var stringBuilder = StringBuilder()
    private var spanRegistry = ArrayList<SpanDeclaration>()
    private var spanStack = Stack<SpanDeclaration>()

    @Suppress("UNCHECKED_CAST")
    constructor(parcel: Parcel) : this() {
        stringBuilder = parcel.readSerializable() as StringBuilder
        spanRegistry = ArrayList(parcel.readArrayList(SpanDeclaration::class.java.classLoader) as ArrayList<SpanDeclaration>)
    }

    fun beginSpan(obj: ParcelableSpan): Spanner {
        spanStack.push(SpanDeclaration(obj, stringBuilder.length, stringBuilder.length))
        return this
    }

    fun appendSpan(text: String, vararg spans: ParcelableSpan): Spanner {
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

    fun build(): Spannable {
        return SpannableString(stringBuilder.toString()).apply {
            spanRegistry.forEach { span ->
                setSpan(span.what, span.start, span.end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        if (spanStack.isNotEmpty()) {
            throw IllegalStateException("Cannot parcel a spanner with an open span")
        }
        parcel.writeSerializable(stringBuilder)
        parcel.writeTypedList(spanRegistry)
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

@Parcelize
data class SpanDeclaration(
        var what: ParcelableSpan,
        var start: Int,
        var end: Int
) : Parcelable
