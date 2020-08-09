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
        RANGES.add(new HealthParameterRange(null, 20f, NONE, () -> createPerformanceBuff(0.1f, 0))); // no task
        RANGES.add(new HealthParameterRange(20f, 50f, NONE, () -> null)); // no task
        RANGES.add(new HealthParameterRange(50f, 60f, COMFORT, () -> createPerformanceBuff(-0.1f, 2))); // task to eat good food
        RANGES.add(new HealthParameterRange(60f, 80f, COMFORT, () -> createPerformanceBuff(-0.25f, 3))); // task to eat good food
        RANGES.add(new HealthParameterRange(80f, null, HEALTH_NEEDS, () -> new HealthTimedBuff(TAG, -1, "hp", 4, iconY))); // task to eat any food
    }

    private Buff createPerformanceBuff(float delta, int iconX) {
        return new HealthBuff(NeedEnum.HUNGER.TAG, delta, "performance", iconX, iconY);
    }
}
