package papyrus.spanner;

import android.text.Spannable;
import android.text.SpannableString;

import java.util.ArrayList;
import java.util.List;

public class Spanner {

    public static class Builder {
        StringBuilder stringBuilder = new StringBuilder();
        List<SpanDeclaration> spanRegistry = new ArrayList<>();

        public Builder appendSpan(String text, Object... spans) {
            for (Object obj : spans) {
                spanRegistry.add(new SpanDeclaration(obj, stringBuilder.length(), stringBuilder.length() + text.length()));
            }
            stringBuilder.append(text);
            return this;
        }

        public Spannable build() {
            SpannableString str = new SpannableString(stringBuilder.toString());
            for (SpanDeclaration span : spanRegistry) {
                str.setSpan(span.what, span.start, span.end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
            }

            return str;
        }

    }

    private static class SpanDeclaration {
        Object what;
        int start;
        int end;

        public SpanDeclaration(Object what, int start, int end) {
            this.what = what;
            this.start = start;
            this.end = end;
        }
    }
}
