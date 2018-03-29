package papyrus.toolbar.tabs.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;

import papyrus.util.Res;


public abstract class TabIndicator {
    protected Paint paint;
    protected int indicatorHeight;

    public TabIndicator() {
        this.paint = new Paint();
        this.indicatorHeight = Res.dpi(3);
    }

    public void onDraw(int left, int right, int bottom, Canvas canvas) {
        canvas.drawRect(left, bottom - indicatorHeight, right, bottom, paint);
    }
}
