package papyrus.core.permissions;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;

import papyrus.core.iface.IPermissionRequester;
import papyrus.util.WeakDelegate;

public class PermissionRequest {
    public IPermissionRequester requester;
    public List<String> alreadyGranted;

    public PermissionRequest(IPermissionRequester requester, List<String> alreadyGranted) {
        if (Context.class.isAssignableFrom(requester.getClass())) {
            this.requester = WeakDelegate.of(requester);
        } else {
            this.requester = requester;
        }
        this.alreadyGranted = alreadyGranted;
    }
}
