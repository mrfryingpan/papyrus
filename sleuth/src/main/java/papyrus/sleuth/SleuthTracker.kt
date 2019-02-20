package papyrus.sleuth

interface SleuthTracker {
    fun eventHandler(): EventHandler

    fun supportedAnnotations(): List<SleuthAnnotation>?
}