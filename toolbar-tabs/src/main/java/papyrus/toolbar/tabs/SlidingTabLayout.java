package papyrus.toolbar.tabs;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;

import papyrus.toolbar.tabs.behavior.DefaultTabScrollBehavior;
import papyrus.toolbar.tabs.behavior.TabScrollBehavior;
import papyrus.toolbar.tabs.iface.ITabFactory;
import papyrus.toolbar.tabs.indicator.TabIndicator;
import papyrus.util.Res;

public class SlidingTabLayout extends HorizontalScrollView implements ViewTreeObserver.OnGlobalLayoutListener {

    private TabStrip mTabStrip;
    private ViewPager pager;
    private TabScrollBehavior tabScrollBehavior = new DefaultTabScrollBehavior();
    private TabIndicator indicator;

    public SlidingTabLayout(Context context) {
        this(context, null);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);

        setupTabStrip();
        setClipChildren(false);

        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private void setupTabStrip() {
        mTabStrip = new TabStrip(getContext());
        mTabStrip.setMinimumHeight(Res.dpi(30));
        mTabStrip.setIndicator(indicator);
        MarginLayoutParams params = new MarginLayoutParams(LayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT);
        params.bottomMargin = (int) Res.dp(2);
        setClipChildren(false);
        removeAllViews();
        addView(mTabStrip, params);
        tabScrollBehavior.initializeTabStrip(mTabStrip);
        populateTabStrip();
    }

    public boolean setViewPager(ViewPager viewPager) {
        if (pager == viewPager) {
            return false;
        } else {
            mTabStrip.removeAllViews();

            pager = viewPager;
            if (viewPager != null) {
                if (viewPager.getAdapter() != null && !(viewPager.getAdapter() instanceof ITabFactory)) {
                    throw new RuntimeException("FIX THIS ERROR: Tabs requires the pager adapter to implement ITabFactory");
                }
                viewPager.getAdapter().registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        setupTabStrip();
                    }
                });
                viewPager.addOnPageChangeListener(new InternalViewPagerListener());
                populateTabStrip();
            }
            return true;
        }
    }


    public void setScrollBehavior(TabScrollBehavior behavior) {
        if (tabScrollBehavior != behavior) {
            tabScrollBehavior = behavior;
            setupTabStrip();
        }
    }

    private void populateTabStrip() {
        mTabStrip.clearTabs();
        ITabFactory tabFactory = null;
        if (pager != null) {
            tabFactory = (ITabFactory) pager.getAdapter();
            final OnClickListener tabClickListener = new TabClickListener(tabFactory);

            for (int i = 0; i < tabFactory.getCount(); i++) {
                Tab tab = tabFactory.getTabView(mTabStrip, i);
                tab.view.setOnClickListener(tabClickListener);
                mTabStrip.addTab(tab);
            }
            if (tabFactory.getCount() > 0) {
                mTabStrip.onPageSelected(pager.getCurrentItem());
            }
        }
        if (tabFactory != null) {
            tabFactory.onTabsCreated(mTabStrip.mTabs);
        }
        invalidate();
    }

    @Override
    public void onGlobalLayout() {
        if (pager != null) {
            tabScrollBehavior.scrollToTab(this, mTabStrip, pager.getCurrentItem(), 0);
        }
    }

    public void setIndicator(TabIndicator indicator) {
        this.indicator = indicator;
        mTabStrip.setIndicator(indicator);
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            tabScrollBehavior.scrollToTab(SlidingTabLayout.this, mTabStrip, position, extraOffset);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
        }

        @Override
        public void onPageSelected(int position) {
            mTabStrip.onPageSelected(position);
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);

                tabScrollBehavior.scrollToTab(SlidingTabLayout.this, mTabStrip, position, 0);
            }
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                mTabStrip.getChildAt(i).setSelected(position == i);
            }
        }

    }

    private class TabClickListener implements OnClickListener {
        ITabFactory factory;

        public TabClickListener(ITabFactory factory) {
            this.factory = factory;
        }

        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    factory.onTabClicked(i);
                    pager.setCurrentItem(i);
                    return;
                }
            }
        }
    }
}