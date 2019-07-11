package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;

/**
 * Stores all attributes of a creature.
 * Does calculations of values based on attributes (move speed, hauling limit).
 *
 * @author Alexander_Kuzyakov on 11.07.2019.
 */
public class AttributeAspect extends Aspect {
    private static final int BASIC_MOVEMENT_DELAY = 60; // 1 tile/sec
    private int agility;
    private int movementDelay;

    public AttributeAspect(Entity entity) {
        super(entity);
    }

    /**
     * Delay in ticks between creature steps. Creature is faster with lesser delay.
     * Creature current wounds, load, influence this.
     */
    public int getMovementDelay() {
        return movementDelay;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    private void updateMovementDelay() {
        movementDelay = BASIC_MOVEMENT_DELAY - agility * 2;
    }
}
