package stonering.widget.util;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import stonering.util.global.StaticSkin;

/**
 * Stack with a progress bar and a label.
 * Label shows progress bar value in 'current/max' format.
 *
 * @author Alexander on 20.12.2019.
 */
public class LabeledProgressBar extends Stack {
    private ProgressBar bar;
    private RatioLabel label;
    private int max;
    private int min;
    private int current;

    public LabeledProgressBar() {
        add(bar = new ProgressBar(0,100, 1, false, StaticSkin.getSkin()));
        add(label = new RatioLabel(0, 100));
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
