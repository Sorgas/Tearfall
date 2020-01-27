package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.unit.AttributesEnum;

import java.util.HashMap;
import java.util.Map;

import static stonering.enums.unit.AttributesEnum.AGILITY;

/**
 * Stores all attributes of a creature.
 * Does calculations of values based on attributes (move speed, hauling limit).
 * <p>
 * Agility - determines creature move speed modifier. which is delay in ticks between creature steps.
 * Creature is faster with higher agility. Creature current wounds, load, also influence this modifier.
 * <p>
 * Endurance - determines creature maxFatigue value. See {@link HealthAspect}.
 * Fatigue is added on work, movement and staying awake and is replenished on sleep.
 *
 * @author Alexander_Kuzyakov on 11.07.2019.
 */
public class AttributeAspect extends Aspect {
    private static final float BASIC_SPEED = 1 / 90f;
    private static final float AGILITY_SPEED_MODIFIER = 0.03f;
    public final Map<AttributesEnum, Integer> attributes;
    public boolean valid;

    public AttributeAspect(Entity entity) {
        super(entity);
        attributes = new HashMap<>();
        for (AttributesEnum value : AttributesEnum.values()) {
            attributes.put(value, 0);
        }
        valid = false;
    }

    public void update(AttributesEnum attribute, int delta) {
        attributes.put(attribute, attributes.get(attribute) + delta);
    }
}
