package papyrus.ui.activity;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

import papyrus.toolbar.Action;
import papyrus.toolbar.PapyrusToolbar;
import papyrus.toolbar.tabs.TabbedPapyrusToolbar;
import papyrus.toolbar.tabs.indicator.SolidTabIndicator;
import papyrus.ui.R;
import papyrus.ui.fragment.PapyrusFragment;
import papyrus.util.Res;

public class PapyrusToolbarActivity extends PapyrusActivity {
    protected TabbedPapyrusToolbar mToolbar;
    private View mOverflowWrapper;
    private FrameLayout mOverflow;

    @Override
    protected int getLayoutID() {
        return R.layout.papyrus_toolbar;
    }

    @Override
    protected void onContentWrapperInflated() {
        mToolbar = findViewById(R.id.toolbar);
        mOverflowWrapper = findViewById(R.id.toolbar_overflow_wrapper);
        mOverflow = findViewById(R.id.toolbar_overflow);
        mOverflowWrapper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mOverflowWrapper.setVisibility(View.GONE);
                return true;
            }
        });
    }

    /*
     *  Toolbar
     */

    protected void setNavigation(int resID, View.OnClickListener onClick) {
        mToolbar.setNavigation(resID, onClick);
    }

    public void setBackNavigation() {
        mToolbar.setNavigation(R.drawable.ic_action_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setTitle(int titleID) {
        mToolbar.setTitle(Res.string(titleID));
    }

    public void setTitle(String title) {
        mToolbar.setTitle(title);
    }

    public void setActions(Action... actions) {
        mToolbar.setActions(actions);
    }

    public void setActions(List<Action> actions) {
        mToolbar.setActions(actions);
    }

    @Override
    protected void popBackstack() {
        super.popBackstack();
        onContentUpdated();
    }

    public void onContentUpdated() {
        super.onContentUpdated();
        configureToolbar(mToolbar);
        if (mCurrentFragment != null && mCurrentFragment instanceof PapyrusFragment) {
            ((PapyrusFragment) mCurrentFragment).configureToolbar(mToolbar);
        }
    }

    protected void configureToolbar(PapyrusToolbar toolbar) {
        mToolbar.setTitle("");
        mToolbar.setTabContentPager(null);
        mToolbar.setActions();
        mToolbar.clearCustom();
        mToolbar.setTabIndicator(new SolidTabIndicator(Color.WHITE));
        mToolbar.setToolbarBackground(Color.RED);
    }

    public void bindPager(ViewPager pager) {
        mToolbar.setTabContentPager(pager);
        onContentUpdated();
    }

    protected void reset() {
        super.reset();
        mToolbar.setActions();
        mToolbar.setTabContentPager(null);
    }
}
