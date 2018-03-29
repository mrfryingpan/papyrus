package papyrus.alerts;

public abstract class DialogCallbacks {
    public abstract void onPositive();

    public void onNegative() {
        otherwise();
    }

    public void onCancel() {
        otherwise();
    }

    public void otherwise() {
        //Intentional Stub
    }
}
