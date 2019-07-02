package papyrus.toolbar.tabs;

import android.view.View;

public class Tab {
    public View view;

    public Tab(View tabView) {
        this.view = tabView;
    }

    /*package*/ void dispatchSelectionStateChanged(boolean selected) {
        onSelectedStateChanged(view, selected);
    }

    public void onSelectedStateChanged(View view, boolean selected) {
        //intentional Stub
    }
}
