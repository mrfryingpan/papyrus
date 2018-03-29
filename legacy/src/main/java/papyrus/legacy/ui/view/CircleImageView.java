package papyrus.legacy.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import papyrus.legacy.PapyrusApp;
import papyrus.legacy.R;
import papyrus.legacy.util.Res;

//TODO
public class CircleImageView extends ImageView {
    private Bitmap renderedDrawable = null;
    private Canvas renderedCanvas = null;
    private RectF circleRect = new RectF();
    private Paint drawPaint;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private int borderWidth;
    private int padding = Res.dpi(2);
    private PorterDuffXfermode srcIn = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawPaint = new Paint();
        drawPaint.setAntiAlias(true);
        drawPaint.setFilterBitmap(true);
        drawPaint.setDither(true);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleImageView,
                0, 0);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CircleImageView_border_color) {
                setBorderColor(a.getColor(attr, Color.TRANSPARENT));
            } else if (attr == R.styleable.CircleImageView_border_width) {
                setBorderWidth(a.getDimensionPixelSize(attr, 2));
            }
        }
    }

    public void clear() {
        housekeep();
    }


    @Override
    protected void onDraw(Canvas baseCanvas) {
        if (getDrawable() != null && mWidth != 0 && mHeight != 0) {
            renderedDrawable.eraseColor(Color.TRANSPARENT);
            renderedCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
            super.onDraw(renderedCanvas);

            BitmapShader shader = new BitmapShader(renderedDrawable, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            drawPaint.setShader(shader);
            drawPaint.setAntiAlias(true);
            baseCanvas.drawOval(circleRect, drawPaint);
        }

        if (borderWidth != 0) {
            baseCanvas.drawArc(circleRect, 0, 360, false, mPaint);
        }
    }

    public void setBorderColor(int color) {
        mPaint.setColor(color);
    }

    public void setBorderWidth(int width) {
        borderWidth = width;
        mPaint.setStrokeWidth(width);

        housekeep();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != 0 && h != 0) {
            mWidth = w;
            mHeight = h;
        }
        housekeep();
    }

    private void housekeep() {
        if (mWidth > 0 && mHeight > 0) {
            circleRect = new RectF((borderWidth + padding) / 2, (borderWidth + padding) / 2, mWidth - (borderWidth + padding) / 2, mHeight - (borderWidth + padding) / 2);
            if (renderedDrawable != null) {
                renderedDrawable.recycle();
            }
            renderedDrawable = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
            renderedCanvas = new Canvas(renderedDrawable);
        }
    }
}