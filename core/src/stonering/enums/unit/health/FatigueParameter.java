package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthBuff;
import stonering.entity.unit.aspects.health.HealthTimedBuff;

import static stonering.enums.action.TaskPriorityEnum.*;

/**
 * Checks value of Creates fatigue buffs.
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
        ranges.add(new HealthParameterRange(0, 20, NONE, () -> createBuff(10, 0)));
        ranges.add(new HealthParameterRange(20, 50, NONE, () -> null));
        ranges.add(new HealthParameterRange(50, 60, COMFORT, () -> createBuff(-10, 2)));
        ranges.add(new HealthParameterRange(60, 80, HEALTH_NEEDS, () -> createBuff(-25, 3)));
        ranges.add(new HealthParameterRange(80, 101, SAFETY,
                () -> new HealthTimedBuff(HealthParameterEnum.HUNGER.TAG, -1, "hp", 4, iconY)));
    }

    private Buff createBuff(int delta, int iconX) {
        return new HealthBuff(tag, delta, "performance", iconX, iconY);
    }
}
