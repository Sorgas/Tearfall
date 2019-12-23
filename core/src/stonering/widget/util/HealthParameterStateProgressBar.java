package stonering.widget.util;

import stonering.entity.unit.aspects.health.HealthParameterState;

/**
 * Progress bar that shows state of creature need.
 *
 * @author Alexander on 23.12.2019.
 */
public class HealthParameterStateProgressBar extends RatioLabelProgressBar {
    private HealthParameterState state;

    public HealthParameterStateProgressBar(HealthParameterState state) {
        super(0, (int) state.max);
        this.state = state;
        bar.setValue(state.current);
        label.setDividend((int) state.current);
    }
}
