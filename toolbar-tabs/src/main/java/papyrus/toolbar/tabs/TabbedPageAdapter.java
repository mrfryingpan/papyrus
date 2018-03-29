package papyrus.toolbar.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

import papyrus.pager.PageAdapter;
import papyrus.pager.iface.IPageCreator;
import papyrus.toolbar.tabs.iface.ITabFactory;
import papyrus.toolbar.tabs.iface.ITabbedPageCreator;
import papyrus.toolbar.tabs.iface.ITabbedPageStateCallback;
import papyrus.util.PapyrusUtil;

public class TabbedPageAdapter extends PageAdapter implements ITabFactory {

    List<Tab> tabs;

    public TabbedPageAdapter(FragmentManager fm, IPageCreator creator) {
        super(fm, creator);
    }

    public TabbedPageAdapter(FragmentManager fm, IPageCreator creator, int initialPosition) {
        super(fm, creator, initialPosition);
    }

    @Override
    protected void onPageCreated(Fragment fragment, int position) {
        super.onPageCreated(fragment, position);
        if (!PapyrusUtil.isEmpty(tabs)) {
            ((ITabbedPageStateCallback) fragment).setTab(tabs.get(position));
        }
    }

    @Override
    public Tab getTabView(TabStrip parent, int position) {
        if (creator instanceof ITabbedPageCreator) {
            return ((ITabbedPageCreator) creator).createTab(parent, contents.get(position));
        } else {
            return null;
        }
    }

    @Override
    public void onTabClicked(int position) {
        Fragment fragment = fragments.get(position) == null ? null : fragments.get(position).get();
        if (fragment instanceof ITabbedPageStateCallback) {
            ((ITabbedPageStateCallback) fragment).onTabClicked(position == currentPosition);
        }
    }

    @Override
    public void onTabsCreated(List<Tab> tabs) {
        this.tabs = tabs;
        for (int i = 0; i < getCount(); i++) {
            Fragment fragment = fragments.get(i) == null ? null : fragments.get(i).get();
            if (fragment != null && fragment instanceof ITabbedPageStateCallback) {
                ((ITabbedPageStateCallback) fragment).setTab(tabs.get(i));
            }
        }
    }
}
