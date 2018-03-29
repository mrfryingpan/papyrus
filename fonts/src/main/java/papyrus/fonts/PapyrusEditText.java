package papyrus.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

public class PapyrusEditText extends android.support.v7.widget.AppCompatEditText {
    public PapyrusEditText(Context context) {
        super(context);
    }

    public PapyrusEditText(Context context, AttributeSet attrs) {
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
