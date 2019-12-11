package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthBuff;
import stonering.entity.unit.aspects.health.HealthTimedBuff;

import static stonering.enums.action.TaskPriorityEnum.*;
import static stonering.enums.action.TaskPriorityEnum.SAFETY;

/**
 * @author Alexander on 06.10.2019.
 */
public class HungerParameter extends HealthParameter {
    private final int iconY = 1;

    public HungerParameter(String tag) {
        super(tag);
    }

    @Override
    protected void fillRanges() {
        ranges.add(new HealthParameterRange(0, 20, NONE, () -> createBuffWithDelta(10, 0)));
        ranges.add(new HealthParameterRange(20, 50, NONE, () -> null));
        ranges.add(new HealthParameterRange(50, 60, COMFORT, () -> createBuffWithDelta(-10, 2)));
        ranges.add(new HealthParameterRange(60, 80, HEALTH_NEEDS, () -> createBuffWithDelta(-25, 3)));
        ranges.add(new HealthParameterRange(80, 101, SAFETY, () -> new HealthTimedBuff(tag, -1, "hp", 4, iconY)));
    }

    private Buff createBuffWithDelta(int delta, int iconX) {
        return new HealthBuff(HealthParameterEnum.HUNGER.TAG, delta, "performance", iconX, iconY);
    }
}
