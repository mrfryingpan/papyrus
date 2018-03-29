package papyrus.toolbar.tabs.behavior;

import android.view.View;

import papyrus.toolbar.tabs.SlidingTabLayout;
import papyrus.toolbar.tabs.TabStrip;

public abstract class TabScrollBehavior {

    protected View tabAtPosition(TabStrip tabStrip, int tabIndex) {
        return tabStrip.getChildAt(tabIndex);
    }

    public void initializeTabStrip(TabStrip tabStrip) {
        // Optional Override
    }

    public abstract void scrollToTab(SlidingTabLayout slidingTabLayout, TabStrip mTabStrip, int tabIndex, int positionOffset);
}
