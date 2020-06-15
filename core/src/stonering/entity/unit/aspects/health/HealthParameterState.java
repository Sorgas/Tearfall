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
    
    public boolean applyDelta(float delta) {
        float oldValue = getRelativeValue();
        current = Math.max(current + delta, 0);
        if (current > max) return true;
        HealthParameterRange oldRange = parameter.PARAMETER.getRange(oldValue);
        return parameter.PARAMETER.getRange(getRelativeValue()) != oldRange; // return true, if range is changed
    }
}
