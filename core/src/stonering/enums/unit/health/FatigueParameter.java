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
    }

    @Override
    protected void fillRanges() {
        RANGES.add(new HealthParameterRange(-1, 20, NONE, () -> createBuff(10, 0)));
        RANGES.add(new HealthParameterRange(20, 50, NONE, () -> null));
        RANGES.add(new HealthParameterRange(50, 60, COMFORT, () -> createBuff(-10, 2)));
        RANGES.add(new HealthParameterRange(60, 80, HEALTH_NEEDS, () -> createBuff(-25, 3)));
        RANGES.add(new HealthParameterRange(80, 101, SAFETY,
                () -> new HealthTimedBuff(HealthParameterEnum.HUNGER.TAG, -1, "hp", 4, iconY)));
    }

    private Buff createBuff(int delta, int iconX) {
        return new HealthBuff(TAG, delta, "performance", iconX, iconY);
    }
}
