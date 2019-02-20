package papyrus.sleuth

interface EventHandler {
    fun send(event: SleuthEvent)
}