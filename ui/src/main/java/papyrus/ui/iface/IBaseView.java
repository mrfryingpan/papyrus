package papyrus.ui.iface;

import android.view.View;

import papyrus.ui.fragment.PapyrusFragment;

public interface IBaseView {
    void showShortSnackbar(String message);

    void showLongSnackbar(String message);

    void showActionSnackbar(String message, String actionText, View.OnClickListener actionClick);

    void dismissSnackbar();

    void dismissKeyboard();

    void finish();

    void simulateBackPress();

    void addContentToBackstack(PapyrusFragment fragment, int... animations);
}
