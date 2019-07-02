package papyrus.toolbar.tabs.iface;

import papyrus.pager.PageItem;
import papyrus.pager.iface.IPageCreator;
import papyrus.toolbar.tabs.Tab;
import papyrus.toolbar.tabs.TabStrip;

public interface ITabbedPageCreator extends IPageCreator {
    Tab createTab(TabStrip parent, PageItem item);
}
