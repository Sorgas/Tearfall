package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthBuff;
import stonering.entity.unit.aspects.health.HealthTimedBuff;

import static stonering.enums.TaskPrioritiesEnum.*;
import static stonering.enums.TaskPrioritiesEnum.SAFETY;

/**
 * Buffs for thirst.
 *
 * @author Alexander on 08.10.2019.
 */
public class ThirstParameter extends HealthParameter {
    private final int iconY = 2;

    public ThirstParameter(String tag) {
        super(new int[]{20, 50, 60, 80, 101}, tag);
    }

    @Override
    protected void fillPriorities() {
        priorities[0] = NONE;
        priorities[1] = NONE;
        priorities[2] = COMFORT;
        priorities[3] = HEALTH_NEEDS;
        priorities[4] = SAFETY;
    }

    @Override
    public Buff getBuffForRange(int rangeIndex) {
        switch (rangeIndex) {
            case 0:
                return createBuffWithDelta(10, 0);
            case 1:
                return null;
            case 2:
                return createBuffWithDelta(-10, 2);
            case 3:
                return createBuffWithDelta(-25, 3);
            case 4:
                return new HealthTimedBuff(tag, -1, "hp", 4, iconY);
        }
        return null;
    }

    private Buff createBuffWithDelta(int delta, int iconX) {
        return new HealthBuff(tag, delta, "performance", iconX, iconY);
    }
}
