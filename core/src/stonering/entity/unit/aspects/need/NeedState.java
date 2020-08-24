package stonering.entity.unit.aspects.need;

import stonering.enums.unit.need.Need;
import stonering.game.model.GamePlayConstants;
import stonering.game.model.system.unit.NeedSystem;

/**
 * State of {@link Need}. Current value is constantly increased by {@link NeedSystem}, and doing satisfying actions,
 * like eating and sleeping, decreases the value.
 * By default, max value of need is 100, but can be changed for specific units, or by buffs.
 *
 * @author Alexander on 06.10.2019.
 */
public class NeedState {
    private float current = 0;
    public float max = GamePlayConstants.DEFAULT_NEED_MAX;

    public float getRelativeValue() {
        return max == 0 ? 0 : current / max * 100f;
    }

    public boolean setValue(float value) {
        current = Math.max(0, value);
        return current > max;
    }

    public boolean changeValue(float delta) {
        return setValue(current + delta);
    }

    public float current() {
        return current;
    }
}
