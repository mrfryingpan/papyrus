package papyrus.pager

import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

import android.util.SparseArray

import java.lang.ref.WeakReference

import papyrus.pager.iface.IPageCreator
import papyrus.pager.iface.IPageStateCallback

open class PageAdapter(fm: FragmentManager, protected var creator: IPageCreator, initialPosition: Int = creator.defaultPage) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT), ViewPager.OnPageChangeListener {
    protected var fragments = SparseArray<WeakReference<Fragment>>()

    var currentPosition: Int = initialPosition
        protected set
    protected var contents: List<PageItem>

    init {
        contents = creator.pages
        onPageSelected(initialPosition)
    }

    fun refresh() {
        fragments.clear()
        contents = creator.pages
        notifyDataSetChanged()
    }

    fun getItemOrNull(position: Int): Fragment? {
        return fragments.get(position)?.get()
    }

    override fun getCount(): Int {
        return contents.size
    }

    override fun getItem(position: Int): Fragment {
        return getItemOrNull(position) ?: run {
            contents[position].newInstance().also { fragment ->
                fragments.put(position, WeakReference(fragment))
                onPageCreated(fragment, position)
            }
        }
    }

    @CallSuper
    protected open fun onPageCreated(fragment: Fragment, position: Int) {
        if (position == currentPosition) {
            onPageSelected(currentPosition)
        }
        (fragment as? IPageStateCallback)?.visibilityChanged(position == currentPosition)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        if (position != currentPosition) {
            (getItemOrNull(currentPosition) as? IPageStateCallback)?.visibilityChanged(false)
        }
        currentPosition = position
        (getItemOrNull(currentPosition) as? IPageStateCallback)?.visibilityChanged(true)

        creator.onContentUpdated()
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    fun currentFragment(): Fragment? {
        return getItemOrNull(currentPosition)
    }
}
