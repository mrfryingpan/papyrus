package papyrus.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import papyrus.alerts.DialogCallbacks;
import papyrus.alerts.PapyrusAlertActivity;
import papyrus.core.Papyrus;
import papyrus.core.iface.IPermissionRequester;
import papyrus.core.iface.IResultCallback;
import papyrus.toolbar.Action;
import papyrus.ui.R;
import papyrus.ui.fragment.PapyrusFragment;
import papyrus.ui.iface.IBackHandler;
import papyrus.ui.iface.IBaseView;
import papyrus.util.PapyrusUtil;

public class PapyrusActivity extends AppCompatActivity implements IBaseView {
    private static final String ROOT_FRAGMENT = "root";
    private boolean needsFragmentAdd;

    protected View mLoading;
    protected FrameLayout mContainer;
    protected Fragment mCurrentFragment;
    protected Snackbar mSnackbar;


    protected int getLayoutID() {
        return R.layout.papyrus_base;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.papyrus_core);
        LayoutInflater.from(this).inflate(getLayoutID(), (FrameLayout) findViewById(R.id.content), true);
        onContentWrapperInflated();
        getSupportFragmentManager().addOnBackStackChangedListener(new android.support.v4.app.FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                mCurrentFragment = getSupportFragmentManager().findFragmentById(R.id.content_container);
                onContentUpdated();
            }
        });
    }

    protected void onContentWrapperInflated() {
        mContainer = findViewById(R.id.content_container);
        mLoading = findViewById(R.id.loading_view);
        mLoading.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needsFragmentAdd) {
            needsFragmentAdd = false;
            setContent(mCurrentFragment);
        }
    }

    @Override
    public void onBackPressed() {
        if (mLoading.getVisibility() != View.VISIBLE
                && ((mCurrentFragment instanceof IBackHandler && ((IBackHandler) mCurrentFragment).handleBackPress())
                || (this instanceof IBackHandler) && ((IBackHandler) this).handleBackPress())) {
            return;
        }

        if (hasBackstack()) {
            popBackstack();
        } else {
            finish();
        }
    }

    protected void reset() {
        mCurrentFragment = null;
    }

    /*
     *  Activity Management
     */
    public void startActivity(Class<? extends Activity> destination, Bundle extras, boolean finish) {
        Intent intent = new Intent(this, destination);
        if (extras != null) {
            intent.putExtras(extras);
        }

        startActivity(intent, finish);
    }

    public void startActivityWithClearStack(Class<? extends Activity> destination, Bundle extras) {
        Intent intent = new Intent(this, destination);
        if (extras != null) {
            intent.putExtras(extras);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void startActivity(Intent intent, boolean finish) {
        startActivity(intent);

        if (finish) {
            finish();
        }
    }

    /*
     *  Content Management
     */
    public void addContentToBackstack(PapyrusFragment fragment, int... animations) {
        addContentToBackstack(fragment, null, animations);
    }

    public static void addContentToActiveBackstack(PapyrusFragment fragment, int... animations) {
        Papyrus.addFragmentToActiveBackstack(fragment, R.id.content_container, null, animations);
    }

    public void addContentToBackstack(PapyrusFragment fragment, String name, int... animations) {
        Papyrus.addFragmentToBackstack(this, fragment, R.id.content_container, name, animations);
    }

    public static void addContentToActiveBackstack(PapyrusFragment fragment, String name, int... animations) {
        Papyrus.addFragmentToActiveBackstack(fragment, R.id.content_container, name, animations);
    }

    public static boolean popBackstackTo(String name) {
        return Papyrus.popBackstackTo(name);
    }

    public static boolean popBackstackIncluding(String name) {
        Papyrus.popBackstackIncluding(name);
        return false;
    }

    public void clearBackstack() {
        Papyrus.clearBackstack(this);
    }

    protected boolean hasBackstack() {
        return this.getSupportFragmentManager().getBackStackEntryCount() > 0;
    }

    protected void popBackstack() {
        this.getSupportFragmentManager().popBackStack();
        this.getSupportFragmentManager().executePendingTransactions();
    }

    public void onContentUpdated() {

    }

    public void setContent(Fragment fragment, int... animations) {
        reset();
        mCurrentFragment = fragment;
        mContainer.removeAllViews();
        try {
            clearBackstack();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (animations.length == 2) {
                transaction.setCustomAnimations(animations[0], animations[1]);
            } else if (animations.length == 4) {
                transaction.setCustomAnimations(animations[0], animations[1], animations[2], animations[3]);
            } else if (animations.length != 0) {
                throw new IllegalArgumentException("Animations array must have length of 0, 2, or 4");
            }
            transaction.replace(R.id.content_container, fragment, ROOT_FRAGMENT);
            transaction.commit();

            if (fragment instanceof PapyrusFragment) {
                ((PapyrusFragment) fragment).visibilityChanged(true);
            }
            onContentUpdated();
        } catch (IllegalStateException e) {
            needsFragmentAdd = true;
        }
    }

    public View setContent(int layoutResID) {
        reset();
        View view = LayoutInflater.from(mContainer.getContext()).inflate(layoutResID, mContainer, false);

        mContainer.removeAllViews();
        mContainer.addView(view);

        onContentUpdated();
        return view;
    }

    public void setContent(View view) {
        reset();
        mContainer.removeAllViews();
        mContainer.addView(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        onContentUpdated();
    }

    /*
     *  View
     */
    public void setLoadingVisible(boolean visible) {
        mLoading.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void dismissKeyboard() {
        PapyrusUtil.dismissKeyboard(getCurrentFocus());
    }

    @Override
    public void simulateBackPress() {
        onBackPressed();
    }

    /*
     * Activity Result Processing
     */
    public synchronized void startActivityForResult(Intent intent, IResultCallback callback) {
        Papyrus.navigate()
                .onResult(callback)
                .startActivity(intent);
    }

    public void startActivityForResult(Class<? extends Activity> destination, Bundle extras, IResultCallback callback) {
        Papyrus.navigate()
                .putAll(extras)
                .onResult(callback)
                .start(destination);
    }

    public void requestPermission(final IPermissionRequester requester, final int requestCode, final String... permissions) {
        Papyrus.requestPermissions(requester, permissions);
    }

    public boolean shouldShowRational(String permission) {
        return Papyrus.shouldShowRational(permission);
    }

    public static void start(Class<? extends Activity> destination, Bundle bundle, boolean finish) {
        Papyrus.navigate()
                .putAll(bundle)
                .start(destination);
        if (finish) {
            Papyrus.finishCurrentActivity();
        }
    }

    public static void start(Intent intent, boolean finish) {
        Papyrus.navigate()
                .startActivity(intent);
        if (finish) {
            Papyrus.finishCurrentActivity();
        }
    }

    public static void startForResult(Class<? extends Activity> destination, Bundle bundle, IResultCallback callback) {
        Papyrus.navigate()
                .putAll(bundle)
                .onResult(callback)
                .start(destination);
    }

    public static void startForResult(Intent intent, IResultCallback callback) {
        Papyrus.navigate()
                .onResult(callback)
                .startActivity(intent);
    }

    public static void startWithClearStack(Class<? extends Activity> destination, Bundle bundle) {
        Papyrus.navigate()
                .putAll(bundle)
                .clearTask()
                .start(destination);
    }

    /**
     * @deprecated
     */
    public PapyrusAlertActivity.Handle showAlert(int titleRes, int messageRes, int positiveRes, int negativeRes, DialogCallbacks callback) {
        PapyrusAlertActivity.Handle handle = new PapyrusAlertActivity.Handle();
        new PapyrusAlertActivity.Builder()
                .title(titleRes)
                .message(messageRes)
                .positive(positiveRes)
                .negative(negativeRes)
                .callbacks(callback)
                .show(handle);
        return handle;
    }

    /**
     * @deprecated
     */
    public papyrus.alerts.PapyrusAlertActivity.Handle showAlert(String title, String message, String positive, String negative, final DialogCallbacks callback) {
        PapyrusAlertActivity.Handle handle = new PapyrusAlertActivity.Handle();
        new PapyrusAlertActivity.Builder()
                .title(title)
                .message(message)
                .positive(positive)
                .negative(negative)
                .callbacks(callback)
                .show(handle);
        return handle;
    }

    /*
     * Snackbar
     */
    protected void configureSnackbar(Snackbar snackbar) {
        // Optional Override.
    }

    public void showShortSnackbar(String message) {
        dismissSnackbar();
        mSnackbar = Snackbar.make(mContainer, message, Snackbar.LENGTH_SHORT);
        configureSnackbar(mSnackbar);

        mSnackbar.show();
    }

    public void showLongSnackbar(String message) {
        dismissSnackbar();
        mSnackbar = Snackbar.make(mContainer, message, Snackbar.LENGTH_LONG);
        configureSnackbar(mSnackbar);

        mSnackbar.show();
    }

    public void showActionSnackbar(String message, String actionText, @Nullable final View.OnClickListener actionClick) {
        if (mSnackbar != null) {
            dismissSnackbar();
        }
        mSnackbar = Snackbar.make(mContainer, message, Snackbar.LENGTH_INDEFINITE);
        configureSnackbar(mSnackbar);
        mSnackbar.setAction(actionText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionClick != null) {
                    actionClick.onClick(v);
                }
                dismissSnackbar();
            }
        });
        mSnackbar.show();
    }

    public void dismissSnackbar() {
        if (mSnackbar != null) {
            mSnackbar.dismiss();
            mSnackbar = null;
        }
    }

    public void setActions(Action[] actions) {
        Log.w("PapyrusActivity", String.format("Attempting to set actions on %s, which does not support PapyrusToolbar Actions", getClass().getCanonicalName()));
    }

    public void bindPager(ViewPager pager) {
        Log.w("PapyrusActivity", String.format("Attempting to set actions on %s, which does not support PapyrusToolbar Tabs", getClass().getCanonicalName()));

    }
}
