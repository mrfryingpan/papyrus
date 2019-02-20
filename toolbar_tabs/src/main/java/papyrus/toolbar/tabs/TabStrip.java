package papyrus.toolbar.tabs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import papyrus.toolbar.tabs.indicator.TabIndicator;

public class TabStrip extends LinearLayout {
    private TabIndicator mIndicator;
    /*package*/ List<Tab> mTabs = new ArrayList<>();
    protected int mSelectedPosition;
    private float mSelectionOffset;
    protected int mCurrentPosition = 0;

    public TabStrip(Context context) {
        this(context, null);
    }

    public TabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundColor(Color.TRANSPARENT);
    }

    /*package*/ void setIndicator(TabIndicator indicator) {
        mIndicator = indicator;
    }

    /*package*/ void onViewPagerPageChanged(int position, float positionOffset) {
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        invalidate();
    }

    /*package*/ void clearTabs() {
        mCurrentPosition = 0;
        removeAllViews();
        mTabs.clear();
    }

    /*package*/ void addTab(Tab tab) {
        mTabs.add(tab);
        addView(tab.view);
    }

    /*package*/ void onPageSelected(int position) {
        mTabs.get(mCurrentPosition).dispatchSelectionStateChanged(false);
        mCurrentPosition = position;
        mTabs.get(mCurrentPosition).dispatchSelectionStateChanged(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int height = getHeight();
        final int childCount = getChildCount();

        if (childCount > 0) {
            View selectedTitle = getChildAt(mSelectedPosition);

            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();

            if (mSelectionOffset > 0f && mSelectedPosition < (getChildCount() - 1)) {
                // Draw the selection partway between the tabs
                View nextTitle = getChildAt(mSelectedPosition + 1);
                left = (int) (mSelectionOffset * nextTitle.getLeft() +
                        (1.0f - mSelectionOffset) * left);
                right = (int) (mSelectionOffset * nextTitle.getRight() +
                        (1.0f - mSelectionOffset) * right);
            }

            if (mIndicator != null) {
                mIndicator.onDraw(left, right, height + ((MarginLayoutParams) getLayoutParams()).bottomMargin, canvas);
            }
        }
    }
}