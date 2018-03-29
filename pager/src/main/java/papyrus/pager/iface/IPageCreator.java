package papyrus.pager.iface;

import java.util.List;

import papyrus.pager.PageItem;

public interface IPageCreator {

    List<PageItem> getPages();

    int getDefaultPage();

    void onContentUpdated();
}
