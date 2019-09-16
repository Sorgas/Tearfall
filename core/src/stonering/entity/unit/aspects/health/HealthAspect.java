package stonering.entity.unit.aspects.health;


import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.game.model.system.units.CreatureHealthSystem;

/**
 * Stores health condition of a unit. See {@link CreatureHealthSystem}
 *
 * @author Alexander_Kuzyakov
 */
public class HealthAspect extends Aspect {
    public float fatigue;
    public float maxFatigue;
    public float moveFatigueNoLoad;
    public float moveFatigueFullLoad;

    public HealthAspect(Entity entity) {
        super(entity);
    }
}
