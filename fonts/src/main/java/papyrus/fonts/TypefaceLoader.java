package papyrus.fonts;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class TypefaceLoader {
    private static Map<String, Typeface> typefaces = new HashMap<>();

    public static synchronized Typeface getTypeface(String name, Context context) {
        Typeface font = typefaces.get(name);
        if (font == null) {
            font = Typeface.createFromAsset(context.getAssets(), String.format("font/%s", name));
            typefaces.put(name, font);
        }
        return font;
    }
}
