package stonering.widget.util;

import stonering.entity.unit.aspects.need.NeedState;

/**
 * Progress bar that shows state of creature need.
 * Need state has value [0, 1], so progressBar value is multiplied by 100f.
 *
 * @author Alexander on 23.12.2019.
 */
public class NeedStateProgressBar extends RatioLabelProgressBar {
    private NeedState state;

    public NeedStateProgressBar(NeedState state) {
        super(0, (int) (state.max * 100));
        this.state = state;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        bar.setValue(state.current() * 100f);
        label.setDividend((int) (state.current() * 100));
    }
}
