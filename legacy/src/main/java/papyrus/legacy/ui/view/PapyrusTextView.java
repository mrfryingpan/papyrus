package papyrus.legacy.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import papyrus.legacy.R;
import papyrus.legacy.util.TypefaceLoader;

/**
 * @deprecated - User papyrus.fonts.PapyrusTextView directly
 */
public class PapyrusTextView extends papyrus.fonts.PapyrusTextView {


    private Drawable actionClicked;
    private Drawable actionDefault;
    private Drawable actionDisabled;

    public PapyrusTextView(Context context) {
        super(context);
    }

    public PapyrusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PapyrusTextView,
                0, 0);

        try {
            String font = a.getString(R.styleable.PapyrusTextView_papyrus_font);
            if (!TextUtils.isEmpty(font)) {
                setFont(font);
            }

            actionClicked = a.getDrawable(R.styleable.PapyrusTextView_compatClickedDrawable);
            actionDefault = a.getDrawable(R.styleable.PapyrusTextView_compatDefaultDrawable);
            actionDisabled = a.getDrawable(R.styleable.PapyrusTextView_compatDisabledDrawable);
            if(actionClicked!=null) {
                this.setBackground(actionDefault);
            }
            if(actionDisabled!=null) {
                this.setEnabled(false);
            }

        } finally {
            a.recycle();
        }
    }

    public void setFont(String font) {
        setTypeface(TypefaceLoader.getTypeface(font, getContext()));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(actionClicked!=null && actionDefault!=null && isEnabled()) {
            if (Build.VERSION.SDK_INT < 21) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    this.setBackground(actionClicked);
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    this.setBackground(actionDefault);
                }
            }
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void setEnabled(boolean enabled) {
        if(enabled) {
            this.setBackground(actionDefault);
        } else {
            this.setBackground(actionDisabled);
        }
        super.setEnabled(enabled);
    }

}
