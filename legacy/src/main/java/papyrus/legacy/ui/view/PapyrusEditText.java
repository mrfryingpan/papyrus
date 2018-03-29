package papyrus.legacy.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import papyrus.legacy.R;
import papyrus.legacy.util.TypefaceLoader;

/**
 * @deprecated - User papyrus.fonts.PapyrusEditText directly
 */
public class PapyrusEditText extends papyrus.fonts.PapyrusEditText {

    public PapyrusEditText(Context context) {
        super(context);
    }

    public PapyrusEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
