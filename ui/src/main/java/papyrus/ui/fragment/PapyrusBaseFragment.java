package papyrus.ui.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import papyrus.ui.iface.IBaseView;
import papyrus.util.WeakDelegate;

public abstract class PapyrusBaseFragment extends Fragment implements IBaseView {

    private IBaseView callThrough = WeakDelegate.dummy(IBaseView.class);


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IBaseView) {
            callThrough = WeakDelegate.of((IBaseView) context);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callThrough = WeakDelegate.dummy(IBaseView.class);
    }

    @Override
    public void showShortSnackbar(String message) {
        callThrough.showShortSnackbar(message);
    }

    @Override
    public void showLongSnackbar(String message) {
        callThrough.showLongSnackbar(message);
    }

    @Override
    public void showActionSnackbar(String message, String actionText, View.OnClickListener actionClick) {
        callThrough.showActionSnackbar(message, actionText, actionClick);
    }

    @Override
    public void dismissSnackbar() {
        callThrough.dismissSnackbar();
    }

    @Override
    public void dismissKeyboard() {
        callThrough.dismissKeyboard();
    }

    @Override
    public void finish() {
        callThrough.finish();
    }

    @Override
    public void simulateBackPress() {
        callThrough.simulateBackPress();
    }

    @Override
    public void addContentToBackstack(PapyrusFragment fragment, int... animations) {
        callThrough.addContentToBackstack(fragment, animations);
    }
}
