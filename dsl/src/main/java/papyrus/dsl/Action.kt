package papyrus.dsl


class Action2<T, K> {
    var action: ((T, K) -> Unit)? = null

    operator fun invoke(action: (T, K) -> Unit) {
        this.action = action
    }

    operator fun invoke(t: T, k: K) {
        action?.invoke(t, k)
    }
}

class Action<T> {
    var action: ((T) -> Unit)? = null
    var lastValue: T? = null

    operator fun invoke(action: (T) -> Unit) {
        this.action = action
    }

    operator fun invoke(t: T) {
        action?.invoke(t)
    }
}

class ReturningAction<T, K>(val default: K) {
    var action: ((T) -> K)? = null

    operator fun invoke(action: (T) -> K) {
        this.action = action
    }

    operator fun invoke(t: T): K {
        return action?.invoke(t) ?: default
    }
}

class ReturningEmptyAction<T>() {
    var action: (() -> T)? = null

    operator fun invoke(action: () -> T) {
        this.action = action
    }

    operator fun invoke(): T? {
        return action?.invoke()
    }
}

class EmptyAction {
    var action: (() -> Unit)? = null

    operator fun invoke(action: () -> Unit) {
        this.action = action
    }

    operator fun invoke() {
        action?.invoke()
    }
}