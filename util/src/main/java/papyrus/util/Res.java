package papyrus.util;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ArgbEvaluator;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.util.DisplayMetrics;

public class Res {
    private static Context context;

    public static void init(Application app) {
        context = app;
    }

    private static void throwIfNoContext() {
        if (context == null) {
            throw new RuntimeException("Res must be initialized before use");
        }
    }

    public static DisplayMetrics displayMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    public static float dp(float pixels) {
        return displayMetrics().density * pixels;
    }

    public static int color(int colorId) {
        throwIfNoContext();
        return context.getResources().getColor(colorId);
    }

    public static String string(int stringId) {
        throwIfNoContext();
        return context.getResources().getString(stringId);
    }

    public static String string(int stringId, Object... formatArgs) {
        throwIfNoContext();
        return context.getResources().getString(stringId, formatArgs);
    }

    public static int integer(int intID) {
        throwIfNoContext();
        return context.getResources().getInteger(intID);
    }

    public static String[] stringArray(int arrayID) {
        throwIfNoContext();
        return context.getResources().getStringArray(arrayID);
    }

    public static int[] intArray(int arrayID) {
        throwIfNoContext();
        return context.getResources().getIntArray(arrayID);
    }

    public static Drawable drawable(int drawableID) {
        throwIfNoContext();
        return context.getResources().getDrawable(drawableID);
    }

    public static String plural(int pluralID, int quantity, Object... formatArgs) {
        throwIfNoContext();
        return context.getResources().getQuantityString(pluralID, quantity, formatArgs);
    }

    public static int indentifyDrawable(String name) {
        throwIfNoContext();
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    public static int interpolateColor(float percent, @ColorRes int colorA, @ColorRes int colorB) {
        throwIfNoContext();
        return (int) new ArgbEvaluator().evaluate(percent, color(colorA), color(colorB));
    }

    public static int dpi(int i) {
        throwIfNoContext();
        return (int) dp(i);
    }

    public static boolean bool(int id) {
        throwIfNoContext();
        return context.getResources().getBoolean(id);
    }

    public static Animator animation(int anim_id) {
        throwIfNoContext();
        return AnimatorInflater.loadAnimator(context, anim_id);
    }
}
