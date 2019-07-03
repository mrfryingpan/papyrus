package papyrus.toolbar.tabs.behavior;

import android.view.View;

import papyrus.toolbar.tabs.SlidingTabLayout;
import papyrus.toolbar.tabs.TabStrip;
import papyrus.util.Res;

public class DefaultTabScrollBehavior extends TabScrollBehavior {
    private static final int mTitleOffset = Res.dpi(24);

    public void scrollToTab(SlidingTabLayout slidingTabLayout, TabStrip mTabStrip, int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = tabAtPosition(mTabStrip, tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                targetScrollX -= mTitleOffset;
            }

            slidingTabLayout.scrollTo(targetScrollX, 0);
        }
    }
}
