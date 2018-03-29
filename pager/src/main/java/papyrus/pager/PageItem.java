package papyrus.pager;


import android.support.v4.app.Fragment;

public abstract class PageItem {
    private String mTitle;

    public PageItem(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public abstract Fragment newInstance();
}