package papyrus.fonts;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

public class PapyrusTypefaceSpan extends TypefaceSpan {

    private final Typeface newType;

    public PapyrusTypefaceSpan(Context context, String font) {
        super("");
        newType = TypefaceLoader.getTypeface(font, context);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, newType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, newType);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf) {
        paint.setTypeface(tf);
    }
}
