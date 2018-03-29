package papyrus.legacy.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;

//TODO
public class SheetView extends LinearLayout {
    int height;

    public SheetView(Context context) {
        super(context);
        setBackgroundColor(Color.WHITE);
        setOrientation(VERTICAL);
    }

    public SheetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.WHITE);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Resources.getSystem().getDisplayMetrics().heightPixels / 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getSheetHeight() {
        return height;
    }
}
