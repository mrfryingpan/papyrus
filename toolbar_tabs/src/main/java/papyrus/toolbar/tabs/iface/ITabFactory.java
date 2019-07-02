package papyrus.toolbar.tabs.iface;

import java.util.List;

import papyrus.toolbar.tabs.Tab;
import papyrus.toolbar.tabs.TabStrip;

public interface ITabFactory {
    int getCount();

    Tab getTabView(TabStrip parent, int position);

    void onTabClicked(int position);

    void onTabsCreated(List<Tab> tabs);
}
