package papyrus.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class PapyrusTextView extends android.support.v7.widget.AppCompatTextView {
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
        } finally {
            a.recycle();
        }
    }

    public void setFont(String font) {
        setTypeface(TypefaceLoader.getTypeface(font, getContext()));
    }
}
