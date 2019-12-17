package papyrus.demo.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import papyrus.demo.ui.fragment.MainFragment;
import papyrus.ui.activity.PapyrusToolbarActivity;

public class MainActivity extends PapyrusToolbarActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(new MainFragment());
    }
}
