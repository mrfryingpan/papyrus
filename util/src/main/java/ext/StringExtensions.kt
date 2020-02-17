package ext

import android.text.TextUtils

fun String.fallback(fallback: String?): String? = if (TextUtils.isEmpty(this)) fallback else this

