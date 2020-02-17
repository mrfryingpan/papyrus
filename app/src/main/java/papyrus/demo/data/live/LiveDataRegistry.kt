package papyrus.demo.data.live

object LiveDataRegistry {
    val picker: PickerLiveData by lazy { PickerLiveData().apply { invalidate() } }
}