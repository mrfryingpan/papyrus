package papyrus.toolbar.tabs

import android.content.Context
import android.database.DataSetObserver
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.HorizontalScrollView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import papyrus.toolbar.tabs.behavior.DefaultTabScrollBehavior
import papyrus.toolbar.tabs.behavior.TabScrollBehavior
import papyrus.toolbar.tabs.iface.ITabFactory
import papyrus.toolbar.tabs.indicator.TabIndicator
import papyrus.util.Res

open class SlidingTabLayout @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyle: Int = 0) : HorizontalScrollView(context, attrs, defStyle), OnGlobalLayoutListener {
    private val mTabStrip: TabStrip = TabStrip(context)
    private var pager: ViewPager? = null
    private var tabScrollBehavior: TabScrollBehavior = DefaultTabScrollBehavior()
    private var indicator: TabIndicator? = null

    private fun setupTabStrip() {
        mTabStrip.minimumHeight = Res.dpi(30)
        mTabStrip.setIndicator(indicator)
        val params = MarginLayoutParams(LayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT)
        params.bottomMargin = Res.dp(2f).toInt()
        clipChildren = false
        removeAllViews()
        addView(mTabStrip, params)
        tabScrollBehavior.initializeTabStrip(mTabStrip)
        populateTabStrip()
    }

    fun setViewPager(viewPager: ViewPager?): Boolean {
        return if (pager === viewPager) {
            false
        } else {
            mTabStrip.removeAllViews()
            pager = viewPager
            viewPager?.let {
                (it.adapter as? ITabFactory)
                        ?: throw RuntimeException("FIX THIS ERROR: Tabs requires the pager adapter to implement ITabFactory")
                it.adapter?.registerDataSetObserver(object : DataSetObserver() {
                    override fun onChanged() {
                        super.onChanged()
                        setupTabStrip()
                    }
                })
                viewPager.addOnPageChangeListener(InternalViewPagerListener())
                populateTabStrip()
            }
            true
        }
    }

    fun setScrollBehavior(behavior: TabScrollBehavior) {
        if (tabScrollBehavior !== behavior) {
            tabScrollBehavior = behavior
            setupTabStrip()
        }
    }

    private fun populateTabStrip() {
        mTabStrip.clearTabs()
        (pager?.adapter as? ITabFactory)?.let { factory ->
            val tabClickListener: OnClickListener = TabClickListener(factory)
            for (i in 0 until factory.getCount()) {
                mTabStrip.addTab(factory.getTabView(mTabStrip, i)?.apply {
                    view.setOnClickListener(tabClickListener)
                })
            }
            if (factory.getCount() > 0) {
                pager?.currentItem?.let(mTabStrip::onPageSelected)
            }
            factory.onTabsCreated(mTabStrip.mTabs)
        }

        invalidate()
    }

    override fun onGlobalLayout() {
        pager?.let {
            tabScrollBehavior.scrollToTab(this, mTabStrip, it.currentItem, 0)
        }
    }

    fun setIndicator(indicator: TabIndicator?) {
        this.indicator = indicator
        mTabStrip.setIndicator(indicator)
    }

    private inner class InternalViewPagerListener : OnPageChangeListener {
        private var mScrollState = 0
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            val tabStripChildCount = mTabStrip.childCount
            if (tabStripChildCount == 0 || position < 0 || position >= tabStripChildCount) {
                return
            }
            mTabStrip.onViewPagerPageChanged(position, positionOffset)
            val selectedTitle = mTabStrip.getChildAt(position)
            val extraOffset = positionOffset.toInt() * (selectedTitle?.width ?: 0)
            tabScrollBehavior.scrollToTab(this@SlidingTabLayout, mTabStrip, position, extraOffset)
        }

        override fun onPageScrollStateChanged(state: Int) {
            mScrollState = state
        }

        override fun onPageSelected(position: Int) {
            mTabStrip.onPageSelected(position)
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f)
                tabScrollBehavior.scrollToTab(this@SlidingTabLayout, mTabStrip, position, 0)
            }
            for (i in 0 until mTabStrip.childCount) {
                mTabStrip.getChildAt(i).isSelected = position == i
            }
        }
    }

    private inner class TabClickListener(var factory: ITabFactory) : OnClickListener {
        override fun onClick(v: View) {
            for (i in 0 until mTabStrip.childCount) {
                if (v === mTabStrip.getChildAt(i)) {
                    factory.onTabClicked(i)
                    pager?.currentItem = i
                    return
                }
            }
        }

    }

    init {
        isHorizontalScrollBarEnabled = false
        isFillViewport = true
        setupTabStrip()
        clipChildren = false
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }
}