package papyrus.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import papyrus.pager.PageAdapter;
import papyrus.pager.PageItem;
import papyrus.toolbar.tabs.Tab;
import papyrus.toolbar.tabs.TabStrip;
import papyrus.toolbar.tabs.iface.ITabbedPageCreator;
import papyrus.ui.R;
import papyrus.util.Res;

public abstract class PapyrusTabsFragment extends PapyrusFragment implements ITabbedPageCreator{
    protected ViewPager pager;
    PageAdapter adapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_tabs;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new PageAdapter(getChildFragmentManager(), this);
        pager = view.findViewById(R.id.pager);

        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(adapter);
        pager.setCurrentItem(getDefaultPage());
    }

    protected void refresh() {
        adapter.refresh();
        pager.setCurrentItem(adapter.getCurrentPosition(), false);
    }


    /**
     * Override to start somewhere in the middle
     */
    public int getDefaultPage() {
        return 0;
    }

    public Tab createTab(TabStrip tabStrip, PageItem item) {
        return new Tab(createTabView(tabStrip, item));
    }

    protected View createTabView(TabStrip parent, PageItem item) {
        TextView textView = new TextView(parent.getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        textView.setAllCaps(true);
        textView.setTextColor(Res.color(android.R.color.white));

        int padding = Res.dpi(16);
        textView.setPadding(padding, padding, padding, padding);

        textView.setText(item.getTitle());

        textView.setLayoutParams(new TabStrip.LayoutParams(TabStrip.LayoutParams.WRAP_CONTENT, TabStrip.LayoutParams.WRAP_CONTENT));
        return textView;
    }
}
