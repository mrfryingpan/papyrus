package papyrus.demo;

import android.Manifest;
import android.content.Intent;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.List;

import papyrus.core.Papyrus;
import papyrus.core.iface.IPermissionRequester;
import papyrus.core.iface.IResultCallback;
import papyrus.core.navigation.Navigator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_results).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Papyrus.navigate()
                        .onResult(new IResultCallback() {
                            @Override
                            public void onResult(int resultCode, Intent data) {
                                Toast.makeText(MainActivity.this, data.getStringExtra("stuff"), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .start(QueryActivity.class);

            }
        });

        findViewById(R.id.button_permissions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Papyrus.requestPermissions(new IPermissionRequester() {
                    @Override
                    public void onPermissionsGranted(List<String> permissions) {
                        Toast.makeText(MainActivity.this, TextUtils.join("\n", permissions), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionsDenied(List<String> permissions) {
                        Toast.makeText(MainActivity.this, TextUtils.join("\n", permissions), Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION);

            }
        });
    }
}
