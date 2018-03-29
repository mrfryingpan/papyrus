package papyrus.toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ActionView {
    View mView;
    ImageView icon;
    TextView text;

    public ActionView(View view) {
        mView = view;
        icon = mView.findViewById(R.id.icon);
        text = mView.findViewById(R.id.text);
    }

    public void bind(Action action) {
        if (action.iconRes != 0) {
            icon.setImageResource(action.iconRes);
            icon.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);
        } else {
            text.setText(action.text);
            text.setVisibility(View.VISIBLE);
            icon.setVisibility(View.GONE);
        }
        mView.setOnClickListener(action.onClickListener);
    }
}
