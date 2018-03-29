package papyrus.legacy.ui.fragment;

import android.support.v4.view.ViewPager;

import papyrus.toolbar.Action;
import papyrus.toolbar.PapyrusToolbar;
import papyrus.ui.activity.PapyrusActivity;
import papyrus.util.Res;

/**
 * @deprecated - Use PapyrusFragment or PapyrusTabsFragement if Tabs are required.
 */
public abstract class PapyrusToolbarFragment extends papyrus.ui.fragment.PapyrusTabsFragment {

    private PapyrusActivity getToolbarActivity() {
        try {
            return (PapyrusActivity) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(String.format("%s does not inherit from PapyrusActivity.\nPapyrusToolbar Fragments may only be added to a PapyrusActivity", getActivity().getClass().getName()));
        }
    }

    protected void setTitle(int titleRes) {
        setTitle(Res.string(titleRes));
    }

    protected void setTitle(final String title) {
        doWhenAttached(new Runnable() {
            public void run() {
                getToolbarActivity().setTitle(title);
            }
        });
    }

    protected void setActions(final Action... actions) {
        doWhenAttached(new Runnable() {
            public void run() {
                getToolbarActivity().setActions(actions);
            }
        });
    }

    public void bindPager(final ViewPager pager) {
        doWhenAttached(new Runnable() {
            public void run() {
                getToolbarActivity().bindPager(pager);
            }
        });
    }

    public void configureToolbar(PapyrusToolbar mToolbar) {
        //Provided for Override Configuration
    }
}
