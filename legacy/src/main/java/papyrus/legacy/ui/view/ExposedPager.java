package papyrus.legacy.ui.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import papyrus.legacy.R;
import papyrus.util.Res;

//TODO
public class ExposedPager extends LinearLayout implements ViewPager.OnPageChangeListener {
    ViewPager mPager;
    View leftSpace;
    View rightSpace;

    private int mPageMargin;
    private boolean mLeftAlign;


    public ExposedPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        parseAttrs(attrs);
    }

    public ExposedPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        parseAttrs(attrs);
    }

    private void parseAttrs(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ExposedPager,
                0, 0);

        try {
            float width = a.getDimension(R.styleable.ExposedPager_content_width, Res.dp(50));
            float height = a.getDimension(R.styleable.ExposedPager_content_height, Res.dp(50));
            int offscreenLimit = a.getInt(R.styleable.ExposedPager_offscreen_limit, 3);
            mPageMargin = a.getDimensionPixelSize(R.styleable.ExposedPager_page_margin, Res.dpi(10));
            mLeftAlign = a.getBoolean(R.styleable.ExposedPager_align_left, false);

            setOffscreenPageLimit(offscreenLimit);
            setPagerSize((int) width, (int) height);

            if (mLeftAlign) {
                leftSpace.setVisibility(View.GONE);
                rightSpace.setVisibility(View.GONE);
            }
        } finally {
            a.recycle();
        }
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_exposed_pager, this, true);
        mPager = findViewById(R.id.pager);
        leftSpace = findViewById(R.id.spacer_left);
        rightSpace = findViewById(R.id.spacer_right);
        setClipChildren(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPager.setPageMargin((int) Res.dp(10));
        mPager.addOnPageChangeListener(this);
    }

    public void setPagerSize(int width, int height) {
        LayoutParams params = new LayoutParams(width, height);
        mPager.setLayoutParams(params);
    }

    public void setOffscreenPageLimit(int offscreenLimit) {
        mPager.setOffscreenPageLimit(offscreenLimit);
    }

    public void setAdapter(PagerAdapter adapter) {
        mPager.setAdapter(adapter);
    }

    public boolean canScroll(int dx) {
        return !(dx < 0 && mPager.getCurrentItem() < mPager.getAdapter().getCount() - 1)
                || !(dx > 0 && mPager.getCurrentItem() > 0);
    }

    private Point mCenter = new Point();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenter.x = w / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mLeftAlign) {
            ev.offsetLocation(-mCenter.x + mPager.getWidth() / 2, 0);
        }
        return mPager.dispatchTouchEvent(ev);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (mPager != null) {
            mPager.addOnPageChangeListener(listener);
        }
    }

    public ViewPager getWrappedPager() {
        return mPager;
    }
}
