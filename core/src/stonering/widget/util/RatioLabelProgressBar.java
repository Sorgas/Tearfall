package stonering.widget.util;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import stonering.util.lang.StaticSkin;

/**
 * Stack with a progress bar and a label.
 * Label shows progress bar value in 'current/max' format.
 *
 * @author Alexander on 20.12.2019.
 */
public class RatioLabelProgressBar extends Stack {
    protected ProgressBar bar;
    protected RatioLabel label;
    private int max;
    private int min;
    private int current;

    public RatioLabelProgressBar(int min, int max) {
        add(bar = new ProgressBar(min, max, 1, false, StaticSkin.getSkin()));
        add(label = new RatioLabel(min, max));
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
