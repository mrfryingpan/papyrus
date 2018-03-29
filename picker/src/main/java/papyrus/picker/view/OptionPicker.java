package papyrus.picker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import papyrus.picker.R;
import papyrus.util.Animations;
import papyrus.util.PapyrusUtil;

public class OptionPicker extends RelativeLayout {

    View scrim;
    FrameLayout drawer;
    OptionField field;

    public OptionPicker(Context context) {
        this(context, null);
    }

    public OptionPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OptionPicker(Context context, OptionField field, int pickerLayout) {
        this(context);
        LayoutInflater.from(context).inflate(R.layout.view_picker_sheet, this, true);
        scrim = PapyrusUtil.findViewByID(this, R.id.scrim);
        drawer = PapyrusUtil.findViewByID(this, R.id.drawer);
        View contentView = LayoutInflater.from(context).inflate(pickerLayout, drawer, false);
        field.onPickerCreated(contentView);

        scrim.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }

    public boolean isOpen() {
        return scrim.getVisibility() == View.VISIBLE;
    }

    public void show() {
        if (getParent() == null) {
            ViewGroup contentView = PapyrusUtil.findParentWithID(field, android.R.id.content);
            setVisibility(View.GONE);
            if (contentView != null) {
                contentView.addView(this);
            }
        }

        Animations.fadeIn(scrim);
        Animations.slideInBottom(drawer);
    }

    public void hide() {
        Animations.slideOffBottom(drawer);
        Animations.fadeOut(scrim, new Runnable() {
            @Override
            public void run() {
                ViewParent parent = getParent();
                if (parent != null && parent instanceof ViewGroup && ((ViewGroup) parent).getId() == android.R.id.content) {
                    ((ViewGroup) parent).removeView(OptionPicker.this);
                }
            }
        });
    }
}