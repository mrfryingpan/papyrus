package papyrus.core.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;


public class InterceptorActivity extends Activity {

    private ResultReceiver callback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent thisIntent = getIntent();
        Intent chainIntent = thisIntent.getParcelableExtra("intent");
        callback = thisIntent.getParcelableExtra("callback");

        if ("requestPermissions".equals(chainIntent.getAction())) {
            List<String> permissionsToRequest = chainIntent.getStringArrayListExtra("permissions");
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), 1);
            } else {
                finish();
            }
        } else {
            startActivityForResult(chainIntent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = new Bundle();
        bundle.putParcelable("result", data);
        callback.send(resultCode, bundle);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Bundle bundle = new Bundle();
        bundle.putParcelable("result", new Intent()
                .putExtra("permissions", permissions)
                .putExtra("grantResults", grantResults));
        callback.send(Activity.RESULT_OK, bundle);
        finish();
    }
}
