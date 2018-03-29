package papyrus.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PapyrusUtil {
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static <E> List<E> combine(List<? extends E>... lists) {
        int finalSize = 0;
        for (List list : lists) {
            finalSize += list.size();
        }
        List<E> result = new ArrayList<>(finalSize);
        for (List<? extends E> list : lists) {
            result.addAll(list);
        }

        return result;
    }

    public static void close(Closeable closable) {
        try {
            if (closable != null) {
                if (OutputStream.class.isAssignableFrom(closable.getClass())) {
                    ((OutputStream) closable).flush();
                }
                closable.close();
            }
        } catch (IOException e) {
            Log.w("PapyrusUtil", "Failed Closing");
        }
    }

    public static void dismissKeyboard(@NonNull View view) {
        try {
            ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (NullPointerException e) {
            Log.w("PapyrusUtil", "Could not dismiss keyboard");
        }
    }

    public static <T> T findParentWithID(@NonNull View view, int id) {
        if (view.getId() == id) {
            return (T) view;
        } else {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof View) {
                return findParentWithID((View) parent, id);
            } else {
                return null;
            }
        }
    }

    public static <T> T findViewByID(@NonNull View view, int id) {
        return (T) view.findViewById(id);
    }

    public static boolean is_translucent(Activity activity) {
        TypedValue value = new TypedValue();
        activity.getTheme().resolveAttribute(android.R.attr.windowIsTranslucent, value, true);
        return "true".equals(value.coerceToString());
    }
}