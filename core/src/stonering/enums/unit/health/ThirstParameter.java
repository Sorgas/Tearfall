package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthBuff;
import stonering.entity.unit.aspects.health.HealthTimedBuff;

import static stonering.enums.action.TaskPriorityEnum.*;
import static stonering.enums.action.TaskPriorityEnum.SAFETY;

/**
 * Produces buffs for thirst.
 *
 * @author Alexander on 08.10.2019.
 */
public class ThirstParameter extends HealthParameter {
    private final int iconY = 2;

    public ThirstParameter(String tag) {
        super(tag);
        RANGES.add(new HealthParameterRange(null, 50f, NONE, () -> null));
        RANGES.add(new HealthParameterRange(50f, 60f, COMFORT, () -> createPerformanceBuff(-0.1f, 2)));
        RANGES.add(new HealthParameterRange(60f, 80f, HEALTH_NEEDS, () -> createPerformanceBuff(-0.25f, 0)));
        RANGES.add(new HealthParameterRange(80f, null, SAFETY, () -> new HealthTimedBuff(TAG, -1, "hp", 4, iconY)));
    }

    private Buff createPerformanceBuff(float delta, int iconX) {
        return new HealthBuff(TAG, delta, "performance", iconX, iconY);
    }
}
