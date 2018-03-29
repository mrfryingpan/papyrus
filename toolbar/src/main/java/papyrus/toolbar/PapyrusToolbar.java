package papyrus.toolbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Arrays;
import java.util.List;

import papyrus.fonts.PapyrusTextView;
import papyrus.util.Res;

public class PapyrusToolbar extends FrameLayout {
    LinearLayout toolbarLayout;
    RelativeLayout toolbarContent;
    ImageView navigation;
    PapyrusTextView title;
    RelativeLayout customLayout;
    FrameLayout content;
    LinearLayout actions;
    View shadow;

    public PapyrusToolbar(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PapyrusToolbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    protected int toolbarLayoutRes() {
        return R.layout.content_toolbar;
    }

    private void init(Context context) {
        View toolbar = LayoutInflater.from(context).inflate(toolbarLayoutRes(), this, false);
        onViewCreated(toolbar);
        addView(toolbar);
    }

    @CallSuper
    protected void onViewCreated(View toolbar) {
        toolbarLayout = toolbar.findViewById(R.id.layout_toolbar);
        toolbarContent = toolbar.findViewById(R.id.content_toolbar);
        navigation = toolbar.findViewById(R.id.toolbar_navigation);
        title = toolbar.findViewById(R.id.toolbar_title);
        customLayout = toolbar.findViewById(R.id.layout_custom);
        content = toolbar.findViewById(R.id.toolbar_content);
        actions = toolbar.findViewById(R.id.toolbar_actions);
        shadow = toolbar.findViewById(R.id.shadow);
    }


    public void setNavigation(int resource, OnClickListener listener) {
        ((MarginLayoutParams) title.getLayoutParams()).leftMargin = (int) Res.dp(72);
        navigation.setImageResource(resource);
        navigation.setOnClickListener(listener);
        navigation.setVisibility(View.VISIBLE);
    }

    public void removeNavigation() {
        ((MarginLayoutParams) title.getLayoutParams()).leftMargin = (int) Res.dp(16);
        navigation.setVisibility(View.GONE);
        navigation.setImageResource(0);
        navigation.setOnClickListener(null);
    }

    public void setTitle(String title) {
        if (title != null) {
            this.title.setVisibility(View.VISIBLE);
            this.title.setText(title);
        } else {
            this.title.setVisibility(View.GONE);
        }
    }

    public View injectCustom(@LayoutRes int customID, boolean automaticInsets) {
        View custom = LayoutInflater.from(customLayout.getContext()).inflate(customID, customLayout, false);
        injectCustom(custom, automaticInsets);
        return custom;
    }

    public void injectCustom(View view, boolean automaticInsets) {
        if (automaticInsets) {
            customLayout.setPadding(
                    navigation.getVisibility() == View.VISIBLE ? Res.dpi(72) : Res.dpi(16),
                    0,
                    actions.getWidth() + Res.dpi(16),
                    0);
        } else {
            customLayout.setPadding(0, 0, 0, 0);
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        customLayout.addView(view, params);
        customLayout.setVisibility(View.VISIBLE);
    }

    public void clearCustom() {
        customLayout.removeAllViews();
    }

    public void setTitle(int stringResID) {
        setTitle(getResources().getString(stringResID));
    }

    public void setActions(Action... actions) {
        setActions(Arrays.asList(actions));
    }

    public void setActions(List<Action> actions) {
        this.actions.removeAllViews();
        if (actions != null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (Action action : actions) {
                View view = inflater.inflate(R.layout.toolbar_action, this.actions, false);
                new ActionView(view).bind(action);
                this.actions.addView(view);
            }
        }
        invalidate();
    }

    public void setToolbarBackground(int color) {
        toolbarLayout.setBackgroundColor(color);
    }

    public void setToolbarBackground(Drawable drawable) {
        toolbarLayout.setBackground(drawable);
    }

    public PapyrusTextView getTitleView() {
        return title;
    }

    public void setShadowVisibile(boolean visible) {
        shadow.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
