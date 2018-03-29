package papyrus.core.navigation;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import papyrus.core.iface.IResultCallback;
import papyrus.core.ui.activity.InterceptorActivity;
import papyrus.util.WeakDelegate;

public class Navigator {
    boolean isActivity;
    Context context;
    Set<Integer> flags = new HashSet<>();
    Bundle extras = new Bundle();
    Customizer customizer = WeakDelegate.dummy(Customizer.class);
    String action;
    Uri data;
    IResultCallback resultCallback;

    public Navigator(Activity activity) {
        context = activity;
        isActivity = true;
    }

    public Navigator(Context context) {
        this.context = context;
        flags.add(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public Navigator flags(Integer... newFlags) {
        Collections.addAll(flags, newFlags);
        return this;
    }

    public Navigator action(String action) {
        this.action = action;
        return this;
    }

    public Navigator data(Uri data) {
        this.data = data;
        return this;
    }

    public Navigator putAll(Bundle bundle) {
        extras.putAll(bundle);
        return this;
    }

    public Navigator putByte(String key, byte value) {
        extras.putByte(key, value);
        return this;
    }

    public Navigator putChar(String key, char value) {
        extras.putChar(key, value);
        return this;
    }

    public Navigator putShort(String key, short value) {
        extras.putShort(key, value);
        return this;
    }

    public Navigator putFloat(String key, float value) {
        extras.putFloat(key, value);
        return this;
    }

    public Navigator putCharSequence(String key, CharSequence value) {
        extras.putCharSequence(key, value);
        return this;
    }

    public Navigator putParcelable(String key, Parcelable value) {
        extras.putParcelable(key, value);
        return this;
    }

    public Navigator putParcelableArray(String key, Parcelable[] value) {
        extras.putParcelableArray(key, value);
        return this;
    }

    public Navigator putIntegerArrayList(String key, ArrayList<Integer> value) {
        extras.putIntegerArrayList(key, value);
        return this;
    }

    public Navigator putString(String key, String value) {
        extras.putString(key, value);
        return this;
    }

    public Navigator putStringArrayList(String key, ArrayList<String> value) {
        extras.putStringArrayList(key, value);
        return this;
    }

    public Navigator putSerializable(String key, Serializable value) {
        extras.putSerializable(key, value);
        return this;
    }

    public Navigator putByteArray(String key, byte[] value) {
        extras.putByteArray(key, value);
        return this;
    }

    public Navigator putShortArray(String key, short[] value) {
        extras.putShortArray(key, value);
        return this;
    }

    public Navigator putCharArray(String key, char[] value) {
        extras.putCharArray(key, value);
        return this;
    }

    public Navigator putFloatArray(String key, float[] value) {
        extras.putFloatArray(key, value);
        return this;
    }

    public Navigator putCharSequenceArray(String key, CharSequence[] value) {
        extras.putCharSequenceArray(key, value);
        return this;
    }

    public Navigator putBundle(String key, Bundle value) {
        extras.putBundle(key, value);
        return this;
    }

    public Navigator withCustomization(Customizer customizer) {
        if (customizer != null) {
            this.customizer = customizer;
        }
        return this;
    }

    public Navigator clearTask() {
        flags.add(Intent.FLAG_ACTIVITY_NEW_TASK);
        flags.add(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return this;
    }

    public Navigator onResult(final IResultCallback resultCallback) {
        this.resultCallback = resultCallback;
        return this;
    }

    private Intent prepareIntent(Class<?> destination) {
        return this.prepareIntent(new Intent(context, destination));
    }

    private Intent prepareIntent(Intent intent) {
        int intentFlags = 0;
        for (int flag : flags) {
            intentFlags &= flag;
        }
        intent.setFlags(intentFlags);
        intent.putExtras(extras);
        intent.setData(data);
        intent.setAction(action);
        if (customizer != null) {
            customizer.customize(intent);
        }
        return intent;
    }

    public void start(Class<?> destination) {
        Intent intent = prepareIntent(destination);
        if (Activity.class.isAssignableFrom(destination)) {
            startActivity(intent);
        } else if (Service.class.isAssignableFrom(destination)) {
            startService(intent);
        }
    }

    public void startActivity(Intent intent) {
        if (resultCallback != null) {
            ResultReceiver receiver = new ResultReceiver(new Handler(Looper.getMainLooper())) {
                @Override
                protected void onReceiveResult(int resultCode, Bundle resultData) {
                    super.onReceiveResult(resultCode, resultData);
                    resultCallback.onResult(resultCode, (Intent) resultData.getParcelable("result"));
                }
            };
            new Navigator(isActivity ? (Activity) context : context)
                    .putParcelable("intent", intent)
                    .putParcelable("callback", receiver)
                    .start(InterceptorActivity.class);
        } else {
            context.startActivity(intent);
        }
    }

    public void startService(Intent intent) {
        context.startService(intent);
    }

    public void bindTo(Class<?> destination, ServiceConnection connection) {
        this.bindTo(destination, connection, Context.BIND_AUTO_CREATE);
    }

    public void bindTo(Class<?> destination, ServiceConnection connection, int flags) {
        context.bindService(prepareIntent(destination), connection, flags);
    }
}
