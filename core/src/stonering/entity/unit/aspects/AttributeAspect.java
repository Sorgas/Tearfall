package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;

/**
 * Stores all attributes of a creature.
 * Does calculations of values based on attributes (move speed, hauling limit).
 * <p>
 * Agility - determines creature move speed modifier. which is delay in ticks between creature steps.
 * Creature is faster with higher agility. Creature current wounds, load, also influence this modifier.
 * <p>
 * Endurance - determines creature stamina value{@link StaminaAspect}.
 * Stamina is spent on work and staying awake and is replenished on sleep.
 *
 * @author Alexander_Kuzyakov on 11.07.2019.
 */
public class AttributeAspect extends Aspect {
    private static final int BASIC_MOVEMENT_DELAY = 60; // 1 tile/sec
    public int agility;
    public int endurance;

    public AttributeAspect(Entity entity) {
        super(entity);
    }

    private void updateMovementDelay() {
        entity.getAspect(MovementAspect.class).movementDelay = BASIC_MOVEMENT_DELAY - agility * 2;
    }
}
