package papyrus.pager;

import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;

import java.lang.ref.WeakReference;
import java.util.List;

import papyrus.pager.iface.IPageCreator;
import papyrus.pager.iface.IPageStateCallback;

public class PageAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {
    protected SparseArray<WeakReference<Fragment>> fragments = new SparseArray<>();

    protected int currentPosition;
    protected IPageCreator creator;
    protected List<PageItem> contents;

    public PageAdapter(FragmentManager fm, IPageCreator creator) {
        this(fm, creator, creator.getDefaultPage());
    }

    public PageAdapter(FragmentManager fm, IPageCreator creator, int initialPosition) {
        super(fm);
        this.creator = creator;
        contents = creator.getPages();
        currentPosition = initialPosition;
        onPageSelected(initialPosition);
    }

    public void refresh() {
        fragments.clear();
        contents = creator.getPages();
        notifyDataSetChanged();
    }

    public Fragment getItemOrNull(int position) {
        WeakReference<Fragment> ref = fragments.get(position);
        if (ref != null) {
            Fragment fragment = ref.get();
            if (fragment != null) {
                return fragment;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return contents.size();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = getItemOrNull(position);
        if (fragment == null) {
            fragment = contents.get(position).newInstance();
            fragments.put(position, new WeakReference<>(fragment));
            if (fragment != null) {
                onPageCreated(fragment, position);
            }

        }
        return fragment;
    }

    @CallSuper
    protected void onPageCreated(Fragment fragment, int position) {
        if (fragment instanceof IPageStateCallback) {
            if (position == currentPosition) {
                onPageSelected(currentPosition);
            }
            ((IPageStateCallback) fragment).visibilityChanged(position == currentPosition);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Fragment fragment;
        if (position != currentPosition) {
            fragment = getItemOrNull(currentPosition);
            if (fragment != null && fragment instanceof IPageStateCallback) {
                ((IPageStateCallback) fragment).visibilityChanged(false);
            }
        }
        currentPosition = position;
        fragment = getItemOrNull(currentPosition);
        if (fragment != null && fragment instanceof IPageStateCallback) {
            ((IPageStateCallback) fragment).visibilityChanged(true);
        }

        creator.onContentUpdated();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public Fragment currentFragment() {
        return getItemOrNull(currentPosition);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
