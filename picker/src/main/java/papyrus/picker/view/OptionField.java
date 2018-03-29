package papyrus.picker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import papyrus.picker.R;

public class OptionField<T> extends FrameLayout {
    private int pickerLayout;
    private int fieldLayout;

    private OptionPicker picker;
    private T selectedItem;


    public OptionField(@NonNull Context context) {
        this(context, null);
    }

    public OptionField(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(attrs);
        LayoutInflater.from(getContext()).inflate(fieldLayout, this, true);
        picker = new OptionPicker(context, this, pickerLayout);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show();
            }
        });
    }

    private void parseAttrs(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.OptionField,
                0, 0);

        pickerLayout = a.getResourceId(R.styleable.OptionField_drawer_layout, R.layout.default_option_drawer);
        fieldLayout = a.getResourceId(R.styleable.OptionField_field_layout, R.layout.default_option_drawer);
    }


    public void onPickerCreated(View view) {
        
    }
}
