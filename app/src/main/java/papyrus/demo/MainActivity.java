package papyrus.demo;

import android.os.Bundle;

import papyrus.ui.activity.PapyrusToolbarActivity;

public class MainActivity extends PapyrusToolbarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(new MainFragment());
    }
}
