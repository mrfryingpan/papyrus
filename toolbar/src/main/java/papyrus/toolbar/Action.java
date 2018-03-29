package papyrus.toolbar;

import android.view.View;

public class Action {
    /*package*/ String text = null;
    /*package*/ int iconRes = 0;
    /*package*/ View.OnClickListener onClickListener = null;

    public Action(String text, View.OnClickListener onClickListener) {
        this.text = text;
        this.onClickListener = onClickListener;
    }

    public Action(int resId, View.OnClickListener onClickListener) {
        iconRes = resId;
        this.onClickListener = onClickListener;
    }
}
