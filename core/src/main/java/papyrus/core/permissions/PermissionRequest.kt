package papyrus.core.permissions
import papyrus.core.IPermissionCallback

class PermissionRequest(val callback: IPermissionCallback, var alreadyGranted: ArrayList<String>)
