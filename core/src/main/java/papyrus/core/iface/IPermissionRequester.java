package papyrus.core.iface;

import java.util.List;

public interface IPermissionRequester {
    void onPermissionsGranted(List<String> permissions);

    void onPermissionsDenied(List<String> permissions);
}
