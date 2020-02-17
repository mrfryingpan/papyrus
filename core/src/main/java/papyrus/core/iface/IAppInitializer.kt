package papyrus.core.iface

import android.app.Application

interface IAppInitializer {
    fun onAppCreated(app: Application)
}