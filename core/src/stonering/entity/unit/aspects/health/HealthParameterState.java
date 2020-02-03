package stonering.entity.unit.aspects.health;

import stonering.enums.unit.health.HealthParameterEnum;

/**
 * Parameter of {@link HealthAspect}.
 *
 * @author Alexander on 06.10.2019.
 */
public class HealthParameterState {
    public final HealthParameterEnum parameter;
    public float current = 0;
    public float max = 100;

    public HealthParameterState(HealthParameterEnum parameter) {
        this.parameter = parameter;
    }

    public float getRelativeValue() {
        return max == 0 ? 0 : current / max * 100f;
    }
}
