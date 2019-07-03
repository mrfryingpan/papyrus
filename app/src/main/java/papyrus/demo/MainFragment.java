package papyrus.demo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import papyrus.core.Papyrus;
import papyrus.core.iface.IPermissionRequester;
import papyrus.core.iface.IResultCallback;
import papyrus.ui.activity.PapyrusToolbarActivity;
import papyrus.ui.fragment.PapyrusFragment;

public class MainFragment extends PapyrusFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Analytics.INSTANCE.getTracker().aboutScreenView();

        view.findViewById(R.id.button_results).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Papyrus.navigate()
                        .onResult(new IResultCallback() {
                            @Override
                            public void onResult(int resultCode, Intent data) {
                                Toast.makeText(getContext(), data.getStringExtra("stuff"), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .start(QueryActivity.class);

            }
        });

        view.findViewById(R.id.button_permissions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Papyrus.requestPermissions(new IPermissionRequester() {
                    @Override
                    public void onPermissionsGranted(List<String> permissions) {
                        Toast.makeText(getContext(), TextUtils.join("\n", permissions), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionsDenied(List<String> permissions) {
                        Toast.makeText(getContext(), TextUtils.join("\n", permissions), Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION);

            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }
}
