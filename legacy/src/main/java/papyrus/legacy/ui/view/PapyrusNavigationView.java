package papyrus.legacy.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.NavigationView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import papyrus.legacy.R;

import papyrus.fonts.PapyrusTypefaceSpan;

//TODO
public class PapyrusNavigationView extends NavigationView {
    private String mFont;

    public PapyrusNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PapyrusNavigationView,
                0, 0);

        try {
            String font = a.getString(R.styleable.PapyrusNavigationView_item_font);
            if (!TextUtils.isEmpty(font)) {
                setFont(font);
            }
        } finally {
            a.recycle();
        }
    }

    public void setFont(String font) {
        mFont = font;
        if (!TextUtils.isEmpty(mFont)) {
            Menu m = getMenu();
            for (int i = 0; i < m.size(); i++) {
                MenuItem mi = m.getItem(i);

                SubMenu subMenu = mi.getSubMenu();
                if (subMenu != null && subMenu.size() > 0) {
                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        applyFontToMenuItem(subMenuItem);
                    }
                }

                applyFontToMenuItem(mi);
            }
        }
    }

    @Override
    public void inflateMenu(int resId) {
        super.inflateMenu(resId);
        setFont(mFont);
    }

    private void applyFontToMenuItem(MenuItem mi) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new PapyrusTypefaceSpan(getContext(), mFont), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }
}
