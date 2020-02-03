package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthBuff;
import stonering.entity.unit.aspects.health.HealthTimedBuff;

import static stonering.enums.action.TaskPriorityEnum.*;

/**
 * Parameter for hunger. Provides {@link stonering.game.model.system.unit.CreatureHealthSystem} with ranges, need priorities, and buffs.
 *
 * @author Alexander on 06.10.2019.
 */
public class HungerParameter extends HealthParameter {
    private final int iconY = 1;

    public HungerParameter(String tag) {
        super(tag);
    }

    @Override
    protected void fillRanges() {
        ranges.add(new HealthParameterRange(0, 20, NONE, () -> createBuffWithDelta(0.1f, 0))); // no task
        ranges.add(new HealthParameterRange(20, 50, NONE, () -> null)); // no task
        ranges.add(new HealthParameterRange(50, 60, COMFORT, () -> createBuffWithDelta(-0.1f, 2))); // task to eat good food
        ranges.add(new HealthParameterRange(60, 80, COMFORT, () -> createBuffWithDelta(-0.25f, 3))); // task to eat good food
        ranges.add(new HealthParameterRange(80, 101, HEALTH_NEEDS, () -> new HealthTimedBuff(tag, -1, "hp", 4, iconY))); // task to eat any food
    }

    private Buff createBuffWithDelta(float delta, int iconX) {
        return new HealthBuff(HealthParameterEnum.HUNGER.TAG, delta, "performance", iconX, iconY);
    }
}
