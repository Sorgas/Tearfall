package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthParameterState;

/**
 * Stores relation between {@link HealthParameterState} value and given {@link Buff}.
 *
 * @author Alexander on 06.10.2019.
 */
public abstract class HealthParameter {
    public final float[] ranges;
    public final Buff[] buffs;

    public HealthParameter(float[] ranges) {
        this.ranges = ranges;
        buffs = new Buff[ranges.length];
    }
}
