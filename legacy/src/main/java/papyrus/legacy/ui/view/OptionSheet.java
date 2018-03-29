package papyrus.legacy.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import papyrus.legacy.R;

import java.util.List;

//TODO
public class OptionSheet<T> extends RelativeLayout {
    private SheetView mSheet;
    private boolean isOpen;
    private OptionView mCurrentlySelected;

    LinearLayout optionsLayout;
    FrameLayout headerLayout;

    public OptionSheet(Context context) {
        super(context);
        init(context);
    }

    public OptionSheet(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(context.getResources().getColor(R.color.black_filter));
        mSheet = (SheetView) LayoutInflater.from(context).inflate(R.layout.content_sheet, this, false);
        LayoutParams sheetParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        sheetParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(mSheet, sheetParams);

        optionsLayout = findViewById(R.id.layout_options);
        headerLayout = findViewById(R.id.layout_header);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        mSheet.animate()
                .translationY(0)
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        setVisibility(View.VISIBLE);
                    }
                })
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isOpen = true;
                    }
                })
                .start();
    }

    public void close() {
        mSheet.animate()
                .translationY(mSheet.getSheetHeight())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isOpen = false;
                        setVisibility(View.GONE);
                    }
                })
                .start();
    }

    public void setOptions(List<T> options, Callback<T> callback) {
        setOptions(options, false, callback);
    }

    public void setOptions(List<T> options, final boolean alwaysCallback, final Callback<T> callback) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        headerLayout.removeAllViews();
        headerLayout.addView(callback.createHeaderView(headerLayout));

        optionsLayout.removeAllViews();
        for (final T option : options) {
            View optionView = callback.createOptionView(optionsLayout, option);
            if (optionView.equals(optionsLayout)) {
                throw new RuntimeException("Do not attach View");
            }
            optionView.setTag(option);
            optionView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCurrentlySelected != v) {
                        configureSelected(v);
                        callback.onSheetItemSelected(option);
                    } else if (alwaysCallback) {
                        callback.onSheetItemSelected(option);
                    }

                    close();
                }
            });
            optionsLayout.addView(optionView, params);
        }
        mSheet.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        mSheet.setTranslationY(mSheet.getMeasuredHeight());
    }

    private void configureSelected(View v) {
        if (mCurrentlySelected != null) {
            mCurrentlySelected.configureSelectedState(false);
        }
        mCurrentlySelected = (OptionView) v;
        if (mCurrentlySelected != null) {
            mCurrentlySelected.configureSelectedState(true);
        }
    }

    public void setSelection(T t) {
        if (t == null) {
            configureSelected(null);
        } else {
            for (int i = 0; i < optionsLayout.getChildCount(); i++) {
                View view = optionsLayout.getChildAt(i);
                if (t.equals(view.getTag())) {
                    configureSelected(view);
                }
            }
        }
    }

    public interface Callback<T> {
        void onSheetItemSelected(T item);

        View createHeaderView(ViewGroup parent);

        OptionView createOptionView(ViewGroup parent, T item);
    }
}
