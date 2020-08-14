package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.unit.CreatureAttributesEnum;
import stonering.enums.unit.GamePlayStatsEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores all attributes of a creature.
 * Contains primary and secondary stats.
 * <p>
 * Agility - determines creature move speed modifier. which is delay in ticks between creature steps.
 * Creature is faster with higher agility. Creature current wounds, load, also influence this modifier.
 * <p>
 * Endurance - determines creature maxFatigue value. See {@link HealthAspect}.
 * Fatigue is added on work, movement and staying awake and is replenished on sleep.
 *
 * @author Alexander_Kuzyakov on 11.07.2019.
 */
public class StatAspect extends Aspect {
    private static final float BASIC_SPEED = 1 / 90f;
    private static final float AGILITY_SPEED_MODIFIER = 0.03f;
    public final Map<CreatureAttributesEnum, Integer> stats;
    public final Map<GamePlayStatsEnum, Float> gameplay;
    public boolean valid;

    public StatAspect(Entity entity) {
        super(entity);
        stats = new HashMap<>();
        for (CreatureAttributesEnum value : CreatureAttributesEnum.values()) {
            stats.put(value, 0);
        }
        gameplay = new HashMap<>();
        valid = false;
    }

    public void setPrimary(CreatureAttributesEnum attribute, int delta) {
        stats.put(attribute, stats.get(attribute) + delta);
    }
}
