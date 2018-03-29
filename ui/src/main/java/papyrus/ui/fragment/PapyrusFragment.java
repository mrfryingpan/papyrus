package papyrus.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import papyrus.toolbar.PapyrusToolbar;
import papyrus.toolbar.tabs.Tab;
import papyrus.toolbar.tabs.TabbedPapyrusToolbar;
import papyrus.toolbar.tabs.iface.ITabbedPageStateCallback;

public abstract class PapyrusFragment extends PapyrusBaseFragment implements ITabbedPageStateCallback {
    private LinkedList<Runnable> taskQueue = new LinkedList<>();


    protected boolean onScreen;
    protected boolean isAttached;
    protected Tab tab;


    protected abstract int getLayoutRes();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = getLayoutRes();
        if (container == null || layoutRes == 0) {
            return null;
        }
        return inflater.inflate(layoutRes, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isAttached = true;
        while (taskQueue.size() > 0) {
            taskQueue.pop().run();
        }
        if (onScreen) {
            visibilityChanged(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isAttached = false;
    }

    protected void doWhenAttached(Runnable task) {
        if (isAttached) {
            task.run();
        } else {
            taskQueue.push(task);
        }
    }

    @Override
    public void visibilityChanged(boolean onScreen) {
        this.onScreen = onScreen;
    }

    @Override
    public void onTabClicked(boolean isReclick) {

    }

    @Override
    public void setTab(Tab tab) {
        this.tab = tab;
    }

    public void configureToolbar(PapyrusToolbar mToolbar){
        //Stub
    }

}
