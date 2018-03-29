package papyrus.alerts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import papyrus.core.Papyrus;
import papyrus.core.iface.IResultCallback;
import papyrus.util.Animations;
import papyrus.util.Res;
import papyrus.util.TouchBlocker;

public class PapyrusAlertActivity extends AppCompatActivity {
    private static final int RESULT_POSITIVE = 0;
    private static final int RESULT_NEGATIVE = 1;
    private static final int RESULT_CANCEL = 2;

    private View mAlertRoot;
    private View mAlertDialog;
    private TextView mAlertTitle;
    private TextView mAlertMessage;
    private TextView mAlertPositive;
    private TextView mAlertNegative;

    public static class Builder {
        String title;
        String message;
        String positive;
        String negative;
        DialogCallbacks callbacks;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder title(@StringRes int titleRes) {
            this.title = Res.string(titleRes);
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder message(@StringRes int messageRes) {
            this.message = Res.string(messageRes);
            return this;
        }

        public Builder positive(String positive) {
            this.positive = positive;
            return this;
        }

        public Builder positive(@StringRes int positiveRes) {
            this.positive = Res.string(positiveRes);
            return this;
        }

        public Builder negative(String negative) {
            this.negative = negative;
            return this;
        }

        public Builder negative(@StringRes int negativeRes) {
            this.negative = Res.string(negativeRes);
            return this;
        }

        public Builder callbacks(DialogCallbacks callbacks) {
            this.callbacks = callbacks;
            return this;
        }

        public void show() {
            show(null);
        }

        public void show(Handle canceler) {
            Papyrus.navigate()
                    .putString("title", title)
                    .putString("message", message)
                    .putString("positive", positive)
                    .putString("negative", negative)
                    .putParcelable("handle", canceler)
                    .onResult(new IResultCallback() {
                        @Override
                        public void onResult(int resultCode, Intent data) {
                            if (resultCode == RESULT_POSITIVE) {
                                if (callbacks != null) {
                                    callbacks.onPositive();
                                }
                            } else if (resultCode == RESULT_NEGATIVE) {
                                if (callbacks != null) {
                                    callbacks.onNegative();
                                }
                            } else {
                                if (callbacks != null) {
                                    callbacks.onCancel();
                                }
                            }
                        }
                    })
                    .start(PapyrusAlertActivity.class);
        }
    }

    public static class Handle extends ResultReceiver {
        private static final int DISMISS = 1;

        ResultReceiver handle;

        public Handle() {
            super(new Handler());
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            handle = resultData == null ? null : (ResultReceiver) resultData.getParcelable("handle");
        }

        public void dismiss() {
            if (handle != null) {
                handle.send(DISMISS, new Bundle());
            }
        }
    }

    private static class InternalHandle extends ResultReceiver {
        private WeakReference<PapyrusAlertActivity> instanceRef = new WeakReference<>(null);

        public InternalHandle(PapyrusAlertActivity instance) {
            super(new Handler(Looper.getMainLooper()));
            instanceRef = new WeakReference<>(instance);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            PapyrusAlertActivity instance = instanceRef.get();
            if (instance != null && resultCode == Handle.DISMISS) {
                instance.setResult(RESULT_CANCEL);
                instance.handleBackPress();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        overridePendingTransition(0, 0);

        mAlertRoot = findViewById(R.id.alert_view);
        mAlertDialog = findViewById(R.id.alert_dialog);
        mAlertTitle = findViewById(R.id.alert_title);
        mAlertMessage = findViewById(R.id.alert_message);
        mAlertPositive = findViewById(R.id.button_positive);
        mAlertNegative = findViewById(R.id.button_negative);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }

        String title = getIntent().getExtras().getString("title");
        String message = getIntent().getExtras().getString("message");
        String positive = getIntent().getExtras().getString("positive");
        String negative = getIntent().getExtras().getString("negative");
        ResultReceiver instanceReceiver = getIntent().getExtras().getParcelable("handle");

        if (instanceReceiver != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("handle", new InternalHandle(this));
            instanceReceiver.send(0, bundle);
        }

        if (TextUtils.isEmpty(title)) {
            mAlertTitle.setVisibility(View.GONE);
        } else {
            mAlertTitle.setVisibility(View.VISIBLE);
            mAlertTitle.setText(title);
        }
        mAlertMessage.setText(message);
        if (TextUtils.isEmpty(positive)) {
            mAlertPositive.setVisibility(View.GONE);
        } else {
            mAlertPositive.setVisibility(View.VISIBLE);
            mAlertPositive.setText(positive);
        }
        mAlertPositive.setText(positive);
        if (TextUtils.isEmpty(negative)) {
            mAlertNegative.setVisibility(View.GONE);
        } else {
            mAlertNegative.setVisibility(View.VISIBLE);
            mAlertNegative.setText(negative);
        }

        mAlertPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_POSITIVE);
                handleBackPress();
            }
        });
        mAlertNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_NEGATIVE);
                handleBackPress();
            }
        });
        mAlertRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCEL);
                handleBackPress();
            }
        });
        TouchBlocker.block(mAlertDialog);
        Animations.fadeIn(mAlertRoot);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCEL);
        handleBackPress();
    }

    public void handleBackPress() {
        Animations.fadeOut(mAlertRoot, new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }
}
