package papyrus.toolbar.tabs.iface;

import papyrus.pager.iface.IPageStateCallback;
import papyrus.toolbar.tabs.Tab;

public interface ITabbedPageStateCallback extends IPageStateCallback {

    void onTabClicked(boolean isReclick);

    void setTab(Tab tab);
}
