package papyrus.demo

import android.app.Application

import papyrus.core.Papyrus

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Papyrus.init(this)
    }
}
