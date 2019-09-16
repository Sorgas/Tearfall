package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.unit.Attributes;

import java.util.HashMap;
import java.util.Map;

import static stonering.enums.unit.Attributes.AGILITY;

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
    private static final int BASIC_MOVEMENT_DELAY = 60; // 1 tile/sec
    public final Map<Attributes, Integer> attributes;

    public AttributeAspect(Entity entity) {
        super(entity);
        attributes = new HashMap<>();
        for (Attributes value : Attributes.values()) {
            attributes.put(value, 0);
        }
    }

    private void updateMovementDelay() {
        entity.getAspect(MovementAspect.class).movementDelay = BASIC_MOVEMENT_DELAY - attributes.get(AGILITY) * 2;
    }

    public void update(Attributes attribute, int delta) {
        attributes.put(attribute, attributes.get(attribute) + delta);
    }
}
