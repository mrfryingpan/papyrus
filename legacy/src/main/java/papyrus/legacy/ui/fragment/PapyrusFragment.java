package papyrus.legacy.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import papyrus.alerts.DialogCallbacks;
import papyrus.alerts.PapyrusAlertActivity;
import papyrus.core.Papyrus;
import papyrus.core.iface.IPermissionRequester;
import papyrus.core.iface.IResultCallback;
/**
 * @deprecated - use papyrus.ui.fragment.PapyrusFragment directly,
 */
public abstract class PapyrusFragment extends papyrus.ui.fragment.PapyrusFragment {

    private void maybeFinishActivity() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    public void startActivity(final Class<? extends Activity> destination, final Bundle extras, final boolean finish) {
        Papyrus.navigate()
                .putAll(extras)
                .start(destination);
        if (finish) {
            maybeFinishActivity();
        }
    }

    public void startActivityWithClearStack(final Class<? extends Activity> destination, final Bundle extras) {
        Papyrus.navigate()
                .putAll(extras)
                .clearTask()
                .start(destination);
    }

    public void startActivity(final Intent intent, final boolean finish) {
        Papyrus.navigate().startActivity(intent);
        if (finish) {
            maybeFinishActivity();
        }
    }

    public void requestPermission(final IPermissionRequester requester, final int requestCode, final String... permission) {
        Papyrus.requestPermissions(requester, permission);
    }

    public boolean shouldShowRational(String permission) {
        return Papyrus.shouldShowRational(permission);
    }

    public void backPress() {
        simulateBackPress();
    }

    //TODO
    public void onContentUpdated() {
    }

    //TODO
    public void addContentToBackstack(final PapyrusFragment fragment, final int... animations) {

    }

    public void startActivityForResult(final Intent intent, final IResultCallback callback) {
        Papyrus.navigate()
                .onResult(callback)
                .startActivity(intent);
    }

    public void startActivityForResult(final Class<? extends Activity> destination, final Bundle extras, final IResultCallback callback) {
        Papyrus.navigate()
                .putAll(extras)
                .onResult(callback)
                .start(destination);
    }

    //TODO
    public void setLoadingVisible(final boolean visible) {
    }

    /**
     * @deprecated use papyrus.alerts.PapyrusAlertActivity.Builder
     */
    public PapyrusAlertActivity.Handle showAlert(int titleRes, int messageRes, int positiveRes, int negativeRes, DialogCallbacks callback) {
        PapyrusAlertActivity.Handle handle = new PapyrusAlertActivity.Handle();
        new PapyrusAlertActivity.Builder()
                .title(titleRes)
                .message(messageRes)
                .positive(positiveRes)
                .negative(negativeRes)
                .callbacks(callback)
                .show(handle);

        return handle;
    }

    /**
     * @deprecated use papyrus.alerts.PapyrusAlertActivity.Builder
     */
    public PapyrusAlertActivity.Handle showAlert(String title, String message, String positive, String negative, DialogCallbacks callback) {
        PapyrusAlertActivity.Handle handle = new PapyrusAlertActivity.Handle();
        new PapyrusAlertActivity.Builder()
                .title(title)
                .message(message)
                .positive(positive)
                .negative(negative)
                .callbacks(callback)
                .show(handle);

        return handle;
    }
}
