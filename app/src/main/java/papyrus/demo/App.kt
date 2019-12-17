package papyrus.demo

import android.app.Application

import papyrus.core.Papyrus

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Papyrus.init(this)
    }

    companion object {
        private var instance: App? = null


        fun get(): App? {
            return instance
        }
    }

}
