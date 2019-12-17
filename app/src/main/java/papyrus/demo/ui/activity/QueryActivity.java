package papyrus.demo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

public class QueryActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(200, new Intent().putExtra("stuff", "Things"));
        finish();
    }
}
