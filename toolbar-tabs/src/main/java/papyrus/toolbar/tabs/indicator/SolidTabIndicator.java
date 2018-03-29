package papyrus.toolbar.tabs.indicator;

public class SolidTabIndicator extends TabIndicator {

    public SolidTabIndicator(int indicatorColor) {
        paint.setColor(indicatorColor);
        paint.setAntiAlias(true);
    }
}
