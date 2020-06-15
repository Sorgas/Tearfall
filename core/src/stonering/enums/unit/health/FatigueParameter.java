package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthBuff;
import stonering.entity.unit.aspects.health.HealthTimedBuff;

import static stonering.enums.action.TaskPriorityEnum.*;

/**
 * Creates buffs according to relative value of fatigue.
 *
 * @author Alexander on 06.10.2019.
 */
public class FatigueParameter extends HealthParameter {
    private final int iconY = 0;

    public FatigueParameter(String tag) {
        super(tag);
        RANGES.add(new HealthParameterRange(null, 20f, NONE, () -> createPerformanceBuff(10, 0)));
        RANGES.add(new HealthParameterRange(20f, 50f, NONE, () -> null));
        RANGES.add(new HealthParameterRange(50f, 60f, COMFORT, () -> createPerformanceBuff(-10, 2)));
        RANGES.add(new HealthParameterRange(60f, 80f, HEALTH_NEEDS, () -> createPerformanceBuff(-25, 3)));
        RANGES.add(new HealthParameterRange(80f, null, SAFETY,
                () -> new HealthTimedBuff(HealthParameterEnum.HUNGER.TAG, -1, "hp", 4, iconY)));
    }

    private Buff createPerformanceBuff(int delta, int iconX) {
        return new HealthBuff(TAG, delta, "performance", iconX, iconY);
    }
}
