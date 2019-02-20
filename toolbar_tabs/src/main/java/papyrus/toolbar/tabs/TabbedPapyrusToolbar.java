package papyrus.toolbar.tabs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import papyrus.toolbar.PapyrusToolbar;
import papyrus.toolbar.tabs.behavior.TabScrollBehavior;
import papyrus.toolbar.tabs.indicator.TabIndicator;

public class TabbedPapyrusToolbar extends PapyrusToolbar {

    SlidingTabLayout tabLayout;

    public TabbedPapyrusToolbar(@NonNull Context context) {
        super(context);
    }

    public TabbedPapyrusToolbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected int toolbarLayoutRes() {
        return R.layout.content_toolbar_tabbed;
    }


    @Override
    protected void onViewCreated(View toolbar) {
        super.onViewCreated(toolbar);
        tabLayout = toolbar.findViewById(R.id.toolbar_tabs);
    }


    public boolean setTabContentPager(ViewPager pager) {
        if (pager == null || pager.getAdapter() == null) {
            tabLayout.setVisibility(View.GONE);
            return true;
        } else {
            tabLayout.setVisibility(View.VISIBLE);
            return tabLayout.setViewPager(pager);
        }
    }

    public void setTabScrollBehavior(TabScrollBehavior behavior) {
        tabLayout.setScrollBehavior(behavior);
    }

    public void setTabIndicator(TabIndicator indicator) {
        tabLayout.setIndicator(indicator);
    }
}
