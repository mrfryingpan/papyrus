package papyrus.toolbar.tabs.indicator;

import android.content.res.Resources;
import android.graphics.LinearGradient;
import android.graphics.Shader;

public class GradientTabIndicator extends TabIndicator {
    public GradientTabIndicator(int startColor, int endColor) {
        LinearGradient linearGradient = new LinearGradient(0, 0, Resources.getSystem().getDisplayMetrics().widthPixels, 0, startColor, endColor, Shader.TileMode.REPEAT);
        paint.setShader(linearGradient);
    }
}
