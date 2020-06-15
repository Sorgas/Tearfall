package stonering.entity.unit.aspects.health;

import stonering.enums.unit.health.HealthParameterEnum;
import stonering.enums.unit.health.HealthParameterRange;

/**
 * Parameter of {@link HealthAspect}. By default, max value of parameter is 100, but can be changed for specific units, or by buffs.
 *
 * @author Alexander on 06.10.2019.
 */
public class HealthParameterState {
    public final HealthParameterEnum parameter;
    private float current = 0;
    public float max = 100;

    public HealthParameterState(HealthParameterEnum parameter) {
        this.parameter = parameter;
    }

    public float getRelativeValue() {
        return max == 0 ? 0 : current / max * 100f;
    }

    /**
     * @return true, if range has been changed.
     */
    public boolean setValue(float value) {
        float oldValue = getRelativeValue();
        current = Math.max(0, value);
        if (current > max) return true;
        HealthParameterRange oldRange = parameter.PARAMETER.getRange(oldValue);
        return parameter.PARAMETER.getRange(getRelativeValue()) != oldRange; // return true, if range is changed
    }

    public boolean changeValue(float delta) {
        return setValue(current + delta);
    }

    public float get() {
        return current;
    }
}
