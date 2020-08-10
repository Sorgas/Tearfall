package stonering.widget.util;

import stonering.entity.unit.aspects.need.NeedState;

/**
 * Progress bar that shows state of creature need.
 *
 * @author Alexander on 23.12.2019.
 */
public class HealthParameterStateProgressBar extends RatioLabelProgressBar {
    private NeedState state;

    public HealthParameterStateProgressBar(NeedState state) {
        super(0, (int) state.max);
        this.state = state;
        bar.setValue(state.get());
        label.setDividend((int) state.get());
    }
}
