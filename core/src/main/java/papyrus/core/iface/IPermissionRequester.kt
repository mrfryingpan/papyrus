package papyrus.core.iface

interface IPermissionRequester {
    fun onPermissionsGranted(permissions: List<String>)

    fun onPermissionsDenied(permissions: List<String>)
}
