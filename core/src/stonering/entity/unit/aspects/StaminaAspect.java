package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.needs.RestNeed;

/**
 * Stores stamina(energy) of a unit.
 * Movement and work decreases stamina. Sleep and not working increases.
 * {@link RestNeed} uses this to define priority of a sleep task.
 *
 * @author Alexander on 05.09.2019.
 */
public class StaminaAspect extends Aspect {
    public float stamina; // [0, 100] + endurance modifier.

    public StaminaAspect(Entity entity) {
        super(entity);
    }

    /**
     * Called, when creature makes step {@link MovementAspect}.
     * Value depends, on creature relative load (current/max).
     */
    public void apllyMoveExhaustion() {
        stamina -= entity.getAspect(EquipmentAspect.class).getRelativeLoad();
    }
}
