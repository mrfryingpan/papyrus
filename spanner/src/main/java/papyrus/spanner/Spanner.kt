package papyrus.spanner

import android.text.Spannable
import android.text.SpannableString
import java.util.*

class Spanner {

    private var stringBuilder = StringBuilder()
    private var spanRegistry = ArrayList<SpanDeclaration>()
    private var spanStack = Stack<SpanDeclaration>()

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

    fun build(): Spannable {
        return SpannableString(stringBuilder.toString()).apply {
            spanRegistry.forEach { span ->
                setSpan(span.what, span.start, span.end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }
    }


    inner class SpanDeclaration(internal var what: Any, internal var start: Int, internal var end: Int)
}
