package papyrus.legacy.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

//TODO
public abstract class OptionView extends FrameLayout {

    public OptionView(Context context) {
        super(context);
    }

    public OptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void configureSelectedState(boolean selected);

}
