package ext

fun Boolean.either(onTrue: Any, onFalse: Any): Any {
    return if (this) {
        onTrue
    } else {
        onFalse
    }
}